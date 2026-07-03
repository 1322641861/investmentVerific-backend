package com.ruoyi.modules.invest.plan.budget.service.impl;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetItemMapper;
import com.ruoyi.modules.invest.plan.budget.service.IInvestBudgetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投资预算明细Service业务层处理
 *
 * @author investvf
 */
@Service
public class InvestBudgetItemServiceImpl implements IInvestBudgetItemService {

    @Autowired
    private InvestBudgetItemMapper itemMapper;

    @Override
    public InvestBudgetItem selectInvestBudgetItemById(Long itemId) {
        return itemMapper.selectInvestBudgetItemById(itemId);
    }

    @Override
    public List<InvestBudgetItem> selectInvestBudgetItemList(InvestBudgetItem item) {
        return itemMapper.selectInvestBudgetItemList(item);
    }

    @Override
    public List<InvestBudgetItem> selectItemsByBudgetId(Long budgetId) {
        return itemMapper.selectItemsByBudgetId(budgetId);
    }
}
