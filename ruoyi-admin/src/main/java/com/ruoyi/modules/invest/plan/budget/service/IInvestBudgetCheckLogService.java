package com.ruoyi.modules.invest.plan.budget.service;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetCheckLog;

import java.util.List;

/**
 * 投资预算校验记录Service接口
 *
 * @author investvf
 */
public interface IInvestBudgetCheckLogService {

    InvestBudgetCheckLog selectInvestBudgetCheckLogById(Long logId);

    List<InvestBudgetCheckLog> selectInvestBudgetCheckLogList(InvestBudgetCheckLog log);

    List<InvestBudgetCheckLog> selectLogsByBudgetId(Long budgetId);
}
