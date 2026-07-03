package com.ruoyi.modules.invest.plan.fund.plan.mapper;

import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金配套计划Mapper接口 */
@Mapper
public interface InvestFundPlanMapper {
    InvestFundPlan selectInvestFundPlanById(Long planId);
    InvestFundPlan selectInvestFundPlanByProjectId(Long projectId);
    List<InvestFundPlan> selectInvestFundPlanList(InvestFundPlan plan);
    int insertInvestFundPlan(InvestFundPlan plan);
    int updateInvestFundPlan(InvestFundPlan plan);
    int updatePlanAmounts(InvestFundPlan plan);
    int updateCheckStatus(InvestFundPlan plan);
    int deleteInvestFundPlanByIds(Long[] planIds);
}
