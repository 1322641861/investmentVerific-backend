package com.ruoyi.modules.invest.plan.fund.arrival.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.fund.arrival.domain.InvestFundArrival;
import com.ruoyi.modules.invest.plan.fund.arrival.mapper.InvestFundArrivalMapper;
import com.ruoyi.modules.invest.plan.fund.arrival.service.IInvestFundArrivalService;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanItemMapper;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvestFundArrivalServiceImpl implements IInvestFundArrivalService {
    @Autowired private InvestFundArrivalMapper arrivalMapper;
    @Autowired private InvestFundPlanItemMapper itemMapper;
    @Autowired private InvestFundPlanMapper planMapper;

    public InvestFundArrival selectInvestFundArrivalById(Long arrivalId){return arrivalMapper.selectInvestFundArrivalById(arrivalId);}
    public List<InvestFundArrival> selectInvestFundArrivalList(InvestFundArrival arrival){return arrivalMapper.selectInvestFundArrivalList(arrival);}

    @Transactional(rollbackFor = Exception.class)
    public int insertInvestFundArrival(InvestFundArrival arrival){
        arrival.setCreateBy(ShiroUtils.getLoginName()); arrival.setCreateTime(DateUtils.getNowDate()); if(arrival.getArrivalStatus()==null){arrival.setArrivalStatus("0");}
        int rows=arrivalMapper.insertInvestFundArrival(arrival);
        InvestFundPlanItem item=new InvestFundPlanItem(); item.setItemId(arrival.getItemId()); item.setArrivedAmount(arrival.getArrivedAmount()); item.setUpdateBy(ShiroUtils.getLoginName()); item.setUpdateTime(DateUtils.getNowDate()); itemMapper.addArrivedAmount(item);
        InvestFundPlan plan=planMapper.selectInvestFundPlanById(arrival.getPlanId()); if(plan!=null){ BigDecimal arrived=(plan.getArrivedTotal()==null?BigDecimal.ZERO:plan.getArrivedTotal()).add(arrival.getArrivedAmount()==null?BigDecimal.ZERO:arrival.getArrivedAmount()); plan.setArrivedTotal(arrived); if(plan.getPlanTotal()!=null && arrived.compareTo(plan.getPlanTotal())>=0){plan.setPlanStatus("3");} plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate()); planMapper.updateInvestFundPlan(plan); }
        return rows;
    }
    public int updateInvestFundArrival(InvestFundArrival arrival){arrival.setUpdateBy(ShiroUtils.getLoginName()); arrival.setUpdateTime(DateUtils.getNowDate()); return arrivalMapper.updateInvestFundArrival(arrival);}
    @Transactional(rollbackFor = Exception.class)
    public int voidArrival(Long arrivalId){ InvestFundArrival a=arrivalMapper.selectInvestFundArrivalById(arrivalId); if(a==null || "1".equals(a.getArrivalStatus()))return 0; return arrivalMapper.voidArrival(arrivalId); }

    @Transactional(rollbackFor = Exception.class)
    public int verifyArrival(Long arrivalId, String voucherNo, String attachment) {
        InvestFundArrival a = arrivalMapper.selectInvestFundArrivalById(arrivalId);
        if (a == null || "1".equals(a.getArrivalStatus())) return 0;
        a.setVoucherNo(voucherNo);
        if (attachment != null) a.setAttachment(attachment);
        a.setArrivalStatus("1"); // 已核验
        a.setUpdateBy(ShiroUtils.getLoginName());
        a.setUpdateTime(DateUtils.getNowDate());
        return arrivalMapper.updateInvestFundArrival(a);
    }
}
