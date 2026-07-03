package com.ruoyi.modules.invest.plan.fund.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.fund.check.domain.InvestFundCheckLog;
import com.ruoyi.modules.invest.plan.fund.check.mapper.InvestFundCheckLogMapper;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundItemError;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundPlanValidateResult;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanItemMapper;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.InvestFundPlanMapper;
import com.ruoyi.modules.invest.plan.fund.plan.service.IInvestFundPlanService;
import com.ruoyi.modules.invest.plan.fund.source.domain.InvestFundSource;
import com.ruoyi.modules.invest.plan.fund.source.mapper.InvestFundSourceMapper;
import com.ruoyi.modules.invest.workflow.BpmnProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestFundPlanServiceImpl implements IInvestFundPlanService {
    @Autowired private InvestFundPlanMapper planMapper;
    @Autowired private InvestFundPlanItemMapper itemMapper;
    @Autowired private InvestFundSourceMapper sourceMapper;
    @Autowired private InvestFundCheckLogMapper checkLogMapper;
    @Autowired private BpmnProcessService bpmnProcessService;

    public InvestFundPlan selectInvestFundPlanById(Long planId) { InvestFundPlan p=planMapper.selectInvestFundPlanById(planId); if(p!=null){p.setItemList(itemMapper.selectItemsByPlanId(planId));} return p; }
    public InvestFundPlan selectInvestFundPlanByProjectId(Long projectId) { return planMapper.selectInvestFundPlanByProjectId(projectId); }
    public List<InvestFundPlan> selectInvestFundPlanList(InvestFundPlan plan) { return planMapper.selectInvestFundPlanList(plan); }
    public int insertInvestFundPlan(InvestFundPlan plan) { plan.setCreateBy(ShiroUtils.getLoginName()); plan.setCreateTime(DateUtils.getNowDate()); plan.setDelFlag("0"); if(plan.getPlanStatus()==null){plan.setPlanStatus("0");} if(plan.getPlanCode()==null||plan.getPlanCode().isEmpty()){plan.setPlanCode("FND-"+System.currentTimeMillis());} return planMapper.insertInvestFundPlan(plan); }
    public int updateInvestFundPlan(InvestFundPlan plan) { plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate()); return planMapper.updateInvestFundPlan(plan); }
    public int deleteInvestFundPlanByIds(Long[] planIds) { for(Long id:planIds){itemMapper.deleteByPlanId(id);} return planMapper.deleteInvestFundPlanByIds(planIds); }
    public List<InvestFundPlanItem> selectItemsByPlanId(Long planId) { return itemMapper.selectItemsByPlanId(planId); }

    @Transactional(rollbackFor = Exception.class)
    public int savePlanItems(Long planId, List<InvestFundPlanItem> items) {
        InvestFundPlan plan = planMapper.selectInvestFundPlanById(planId);
        if (plan == null || (!"0".equals(plan.getPlanStatus()) && !"5".equals(plan.getPlanStatus()))) return 0;
        itemMapper.deleteByPlanId(planId);
        BigDecimal total = BigDecimal.ZERO;
        int rows = 0; int order = 1;
        if (items != null) {
            for (InvestFundPlanItem item : items) {
                item.setPlanId(planId);
                item.setOrderNum(item.getOrderNum()==null?order++:item.getOrderNum());
                item.setCreateBy(ShiroUtils.getLoginName()); item.setCreateTime(DateUtils.getNowDate());
                fillSource(item);
                if(item.getPlannedAmount()==null){item.setPlannedAmount(BigDecimal.ZERO);} if(item.getArrivedAmount()==null){item.setArrivedAmount(BigDecimal.ZERO);} if(item.getPaidAmount()==null){item.setPaidAmount(BigDecimal.ZERO);}
                total = total.add(item.getPlannedAmount());
                rows += itemMapper.insertInvestFundPlanItem(item);
            }
        }
        plan.setPlanTotal(total);
        plan.setMatchRate(calcRate(total, plan.getBudgetAmount()));
        plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate());
        planMapper.updatePlanAmounts(plan);
        validatePlan(planId, "1");
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    public FundPlanValidateResult validatePlan(Long planId, String checkType) {
        InvestFundPlan plan = selectInvestFundPlanById(planId);
        FundPlanValidateResult r = new FundPlanValidateResult();
        if (plan == null) { r.setCheckResult("1"); return r; }
        BigDecimal planned = plan.getPlanTotal()==null?BigDecimal.ZERO:plan.getPlanTotal();
        BigDecimal arrived = plan.getArrivedTotal()==null?BigDecimal.ZERO:plan.getArrivedTotal();
        BigDecimal paid = plan.getPaidTotal()==null?BigDecimal.ZERO:plan.getPaidTotal();
        BigDecimal budget = plan.getBudgetAmount()==null?BigDecimal.ZERO:plan.getBudgetAmount();
        r.setBudgetAmount(budget); r.setPlannedTotal(planned); r.setArrivedTotal(arrived); r.setPaidTotal(paid); r.setMatchRate(calcRate(planned,budget)); r.setArrivedRate(calcRate(arrived,budget)); r.setPaidRate(calcRate(paid,arrived));
        if(planned.compareTo(budget)<0){ r.getErrors().add(err("PLAN_BELOW_BUDGET", null, budget, planned, "计划配套金额小于预算金额")); }
        if(paid.compareTo(arrived)>0){ r.getErrors().add(err("PAY_OVER_ARRIVAL", null, arrived, paid, "累计支付金额不能超过累计到账金额")); }
        // 配套率阈值检查
        BigDecimal matchRate = r.getMatchRate();
        if (matchRate.compareTo(new BigDecimal("80")) < 0) {
            r.getErrors().add(err("MATCH_RATE_LOW", null, new BigDecimal("80"), matchRate, "配套率低于80%，请及时安排融资"));
        }
        BigDecimal arrivedRate = r.getArrivedRate();
        if (arrivedRate.compareTo(new BigDecimal("70")) < 0) {
            r.getErrors().add(err("ARRIVED_RATE_LOW", null, new BigDecimal("70"), arrivedRate, "到账率低于70%，请催促资金到位"));
        }
        BigDecimal paidRate = r.getPaidRate();
        if (paidRate.compareTo(new BigDecimal("95")) > 0) {
            r.getErrors().add(err("PAID_RATE_HIGH", null, new BigDecimal("95"), paidRate, "支付覆盖率超过95%，资金即将耗尽"));
        }
        r.setCheckResult(r.getErrors().isEmpty()?"0":"1");
        InvestFundCheckLog log = new InvestFundCheckLog(); log.setPlanId(planId); log.setProjectId(plan.getProjectId()); log.setBudgetAmount(budget); log.setPlannedTotal(planned); log.setArrivedTotal(arrived); log.setPaidTotal(paid); log.setMatchRate(r.getMatchRate()); log.setArrivedRate(r.getArrivedRate()); log.setPaidRate(r.getPaidRate()); log.setCheckResult(r.getCheckResult()); log.setCheckType(checkType==null?"1":checkType); log.setErrorJson(JSON.toJSONString(r.getErrors())); log.setCreateBy(ShiroUtils.getLoginName()); log.setCreateTime(DateUtils.getNowDate()); checkLogMapper.insertInvestFundCheckLog(log);
        plan.setLastCheckStatus(r.getCheckResult()); plan.setLastCheckTime(DateUtils.getNowDate()); plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate()); planMapper.updateCheckStatus(plan);
        return r;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean submitPlan(Long planId) {
        FundPlanValidateResult r = validatePlan(planId, "2"); if(!r.passed()) return false;
        InvestFundPlan plan = planMapper.selectInvestFundPlanById(planId); if(plan==null) return false;
        try { Map<String,Object> vars=new HashMap<>(); vars.put("planId", planId); vars.put("projectId", plan.getProjectId()); ProcessInstance pi=bpmnProcessService.startProcess("fund_plan_approval", String.valueOf(planId), vars); plan.setProcessInstanceId(pi.getId()); } catch(Exception e) { plan.setProcessInstanceId(null); }
        plan.setPlanStatus("1"); plan.setUpdateBy(ShiroUtils.getLoginName()); plan.setUpdateTime(DateUtils.getNowDate()); return planMapper.updateInvestFundPlan(plan)>0;
    }

    private void fillSource(InvestFundPlanItem item){ if(item.getSourceId()==null)return; InvestFundSource s=sourceMapper.selectInvestFundSourceById(item.getSourceId()); if(s!=null){item.setSourceCode(s.getSourceCode()); item.setSourceName(s.getSourceName()); item.setSourceType(s.getSourceType());} }
    private BigDecimal calcRate(BigDecimal num, BigDecimal den){ if(den==null||den.compareTo(BigDecimal.ZERO)==0)return BigDecimal.ZERO; return (num==null?BigDecimal.ZERO:num).multiply(new BigDecimal("100")).divide(den,2, RoundingMode.HALF_UP); }
    private FundItemError err(String rule, InvestFundPlanItem item, BigDecimal expected, BigDecimal actual, String msg){ FundItemError e=new FundItemError(); e.setRule(rule); if(item!=null){e.setItemId(item.getItemId()); e.setSourceName(item.getSourceName());} e.setExpectedAmount(expected); e.setActualAmount(actual); e.setMessage(msg); return e; }
}
