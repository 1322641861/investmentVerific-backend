package com.ruoyi.modules.invest.plan.budget.mapper;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudget;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投资预算Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface InvestBudgetMapper {

    InvestBudget selectInvestBudgetById(Long budgetId);

    InvestBudget selectInvestBudgetBySchemeId(Long schemeId);

    List<InvestBudget> selectInvestBudgetList(InvestBudget budget);

    int insertInvestBudget(InvestBudget budget);

    int updateInvestBudget(InvestBudget budget);

    int updateBudgetAmounts(InvestBudget budget);

    int updateBudgetCheckStatus(InvestBudget budget);

    int deleteInvestBudgetByIds(Long[] budgetIds);
}
