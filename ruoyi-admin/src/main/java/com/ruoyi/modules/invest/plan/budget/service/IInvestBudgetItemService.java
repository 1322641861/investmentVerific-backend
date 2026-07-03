package com.ruoyi.modules.invest.plan.budget.service;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;

import java.util.List;

/**
 * 投资预算明细Service接口
 *
 * @author investvf
 */
public interface IInvestBudgetItemService {

    InvestBudgetItem selectInvestBudgetItemById(Long itemId);

    List<InvestBudgetItem> selectInvestBudgetItemList(InvestBudgetItem item);

    List<InvestBudgetItem> selectItemsByBudgetId(Long budgetId);
}
