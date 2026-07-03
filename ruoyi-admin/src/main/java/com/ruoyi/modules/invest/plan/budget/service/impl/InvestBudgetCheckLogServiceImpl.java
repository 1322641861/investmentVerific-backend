package com.ruoyi.modules.invest.plan.budget.service.impl;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetCheckLog;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetCheckLogMapper;
import com.ruoyi.modules.invest.plan.budget.service.IInvestBudgetCheckLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投资预算校验记录Service业务层处理
 *
 * @author investvf
 */
@Service
public class InvestBudgetCheckLogServiceImpl implements IInvestBudgetCheckLogService {

    @Autowired
    private InvestBudgetCheckLogMapper checkLogMapper;

    @Override
    public InvestBudgetCheckLog selectInvestBudgetCheckLogById(Long logId) {
        return checkLogMapper.selectInvestBudgetCheckLogById(logId);
    }

    @Override
    public List<InvestBudgetCheckLog> selectInvestBudgetCheckLogList(InvestBudgetCheckLog log) {
        return checkLogMapper.selectInvestBudgetCheckLogList(log);
    }

    @Override
    public List<InvestBudgetCheckLog> selectLogsByBudgetId(Long budgetId) {
        return checkLogMapper.selectLogsByBudgetId(budgetId);
    }
}
