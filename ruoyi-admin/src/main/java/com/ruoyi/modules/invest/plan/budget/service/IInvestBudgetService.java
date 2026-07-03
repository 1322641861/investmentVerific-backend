package com.ruoyi.modules.invest.plan.budget.service;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudget;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetCheckLog;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetValidateResult;
import com.ruoyi.modules.invest.project.domain.InvestProject;

import java.util.List;

/**
 * 投资预算Service接口
 *
 * @author investvf
 */
public interface IInvestBudgetService {

    InvestBudget selectInvestBudgetById(Long budgetId);

    InvestBudget selectInvestBudgetBySchemeId(Long schemeId);

    List<InvestBudget> selectInvestBudgetList(InvestBudget budget);

    int insertInvestBudget(InvestBudget budget);

    int updateInvestBudget(InvestBudget budget);

    int deleteInvestBudgetByIds(Long[] budgetIds);

    List<InvestBudgetItem> selectItemsByBudgetId(Long budgetId);

    int saveBudgetItems(Long budgetId, List<InvestBudgetItem> items);

    List<InvestProject> selectCandidateProjects(Long budgetId);

    BudgetValidateResult validateBudget(Long budgetId, String checkType);

    List<InvestBudgetCheckLog> selectCheckLogsByBudgetId(Long budgetId);

    boolean submitBudget(Long budgetId);
}
