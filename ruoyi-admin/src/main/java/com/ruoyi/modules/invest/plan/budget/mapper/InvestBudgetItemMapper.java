package com.ruoyi.modules.invest.plan.budget.mapper;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投资预算明细Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface InvestBudgetItemMapper {

    InvestBudgetItem selectInvestBudgetItemById(Long itemId);

    List<InvestBudgetItem> selectInvestBudgetItemList(InvestBudgetItem item);

    List<InvestBudgetItem> selectItemsByBudgetId(Long budgetId);

    int insertInvestBudgetItem(InvestBudgetItem item);

    int batchInsertInvestBudgetItem(List<InvestBudgetItem> items);

    int updateInvestBudgetItem(InvestBudgetItem item);

    int deleteByBudgetId(Long budgetId);

    int deleteInvestBudgetItemByIds(Long[] itemIds);
}
