package com.ruoyi.modules.invest.plan.fund.payment.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPayment;
import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPaymentSplit;
import com.ruoyi.modules.invest.plan.fund.payment.mapper.InvestFundPaymentMapper;
import com.ruoyi.modules.invest.plan.fund.payment.mapper.InvestFundPaymentSplitMapper;
import com.ruoyi.modules.invest.plan.fund.payment.service.IInvestFundPaymentService;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanItemMapper;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanMapper;
import com.ruoyi.modules.invest.workflow.BpmnProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestFundPaymentServiceImpl implements IInvestFundPaymentService {
    @Autowired private InvestFundPaymentMapper paymentMapper;
    @Autowired private InvestFundPaymentSplitMapper splitMapper;
    @Autowired private InvestFundPlanMapper planMapper;
    @Autowired private InvestFundPlanItemMapper itemMapper;
    @Autowired private BpmnProcessService bpmnProcessService;

    public InvestFundPayment selectInvestFundPaymentById(Long paymentId){InvestFundPayment p=paymentMapper.selectInvestFundPaymentById(paymentId); if(p!=null){p.setSplitList(splitMapper.selectSplitsByPaymentId(paymentId));} return p;}
    public List<InvestFundPayment> selectInvestFundPaymentList(InvestFundPayment payment){return paymentMapper.selectInvestFundPaymentList(payment);}
    @Transactional(rollbackFor=Exception.class)
    public int insertInvestFundPayment(InvestFundPayment payment){payment.setCreateBy(ShiroUtils.getLoginName()); payment.setCreateTime(DateUtils.getNowDate()); if(payment.getPayStatus()==null){payment.setPayStatus("0");} if(payment.getPaymentCode()==null||payment.getPaymentCode().isEmpty()){payment.setPaymentCode("PAY-"+System.currentTimeMillis());} if(payment.getPaidAmount()==null){payment.setPaidAmount(BigDecimal.ZERO);} int rows=paymentMapper.insertInvestFundPayment(payment); saveSplits(payment); return rows;}
    @Transactional(rollbackFor=Exception.class)
    public int updateInvestFundPayment(InvestFundPayment payment){payment.setUpdateBy(ShiroUtils.getLoginName()); payment.setUpdateTime(DateUtils.getNowDate()); int rows=paymentMapper.updateInvestFundPayment(payment); if(payment.getSplitList()!=null){splitMapper.deleteByPaymentId(payment.getPaymentId()); saveSplits(payment);} return rows;}
    public int deleteInvestFundPaymentByIds(Long[] paymentIds){for(Long id:paymentIds){splitMapper.deleteByPaymentId(id);} return paymentMapper.deleteInvestFundPaymentByIds(paymentIds);}
    @Transactional(rollbackFor=Exception.class)
    public boolean submitPayment(Long paymentId){InvestFundPayment p=selectInvestFundPaymentById(paymentId); if(p==null)return false; if(!checkSplits(p))return false; try{Map<String,Object> vars=new HashMap<>(); vars.put("paymentId",paymentId); vars.put("planId",p.getPlanId()); ProcessInstance pi=bpmnProcessService.startProcess("fund_payment_approval",String.valueOf(paymentId),vars); p.setProcessInstanceId(pi.getId());}catch(Exception e){p.setProcessInstanceId(null);} p.setPayStatus("1"); p.setUpdateBy(ShiroUtils.getLoginName()); p.setUpdateTime(DateUtils.getNowDate()); return paymentMapper.updateInvestFundPayment(p)>0;}
    @Transactional(rollbackFor=Exception.class)
    public boolean payPayment(Long paymentId){InvestFundPayment p=selectInvestFundPaymentById(paymentId); if(p==null||p.getSplitList()==null)return false; if(!checkSplits(p))return false; BigDecimal paid=BigDecimal.ZERO; for(InvestFundPaymentSplit s:p.getSplitList()){InvestFundPlanItem item=new InvestFundPlanItem(); item.setItemId(s.getItemId()); item.setPaidAmount(s.getSplitAmount()); item.setUpdateBy(ShiroUtils.getLoginName()); item.setUpdateTime(DateUtils.getNowDate()); itemMapper.addPaidAmount(item); paid=paid.add(s.getSplitAmount()==null?BigDecimal.ZERO:s.getSplitAmount());} InvestFundPlan plan=planMapper.selectInvestFundPlanById(p.getPlanId()); if(plan!=null){plan.setPaidTotal((plan.getPaidTotal()==null?BigDecimal.ZERO:plan.getPaidTotal()).add(paid)); plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate()); planMapper.updateInvestFundPlan(plan);} p.setPaidAmount(paid); p.setPayStatus("3"); p.setPayDate(DateUtils.getNowDate()); p.setUpdateBy(ShiroUtils.getLoginName()); p.setUpdateTime(DateUtils.getNowDate()); return paymentMapper.updateInvestFundPayment(p)>0;}

    @Transactional(rollbackFor=Exception.class)
    public boolean writeOffPayment(Long paymentId) {
        InvestFundPayment p = paymentMapper.selectInvestFundPaymentById(paymentId);
        if (p == null || !"3".equals(p.getPayStatus())) return false;
        p.setPayStatus("4"); // 已核销
        p.setUpdateBy(ShiroUtils.getLoginName());
        p.setUpdateTime(DateUtils.getNowDate());
        return paymentMapper.updateInvestFundPayment(p) > 0;
    }
    private void saveSplits(InvestFundPayment p){ if(p.getSplitList()==null)return; for(InvestFundPaymentSplit s:p.getSplitList()){s.setPaymentId(p.getPaymentId()); splitMapper.insertInvestFundPaymentSplit(s);} }
    private boolean checkSplits(InvestFundPayment p){BigDecimal sum=BigDecimal.ZERO; if(p.getSplitList()!=null){for(InvestFundPaymentSplit s:p.getSplitList()){sum=sum.add(s.getSplitAmount()==null?BigDecimal.ZERO:s.getSplitAmount());}} return p.getApplyAmount()!=null && sum.compareTo(p.getApplyAmount())==0;}
}
