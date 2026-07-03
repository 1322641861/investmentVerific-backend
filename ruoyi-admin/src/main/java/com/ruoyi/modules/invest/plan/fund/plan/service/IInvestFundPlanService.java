package com.ruoyi.modules.invest.plan.fund.plan.service;

import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundPlanValidateResult;
import java.util.List;

public interface IInvestFundPlanService {
    InvestFundPlan selectInvestFundPlanById(Long planId);
    InvestFundPlan selectInvestFundPlanByProjectId(Long projectId);
    List<InvestFundPlan> selectInvestFundPlanList(InvestFundPlan plan);
    int insertInvestFundPlan(InvestFundPlan plan);
    int updateInvestFundPlan(InvestFundPlan plan);
    int deleteInvestFundPlanByIds(Long[] planIds);
    List<InvestFundPlanItem> selectItemsByPlanId(Long planId);
    int savePlanItems(Long planId, List<InvestFundPlanItem> items);
    FundPlanValidateResult validatePlan(Long planId, String checkType);
    boolean submitPlan(Long planId);
}
