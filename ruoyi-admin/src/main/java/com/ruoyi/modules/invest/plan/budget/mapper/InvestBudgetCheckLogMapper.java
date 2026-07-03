package com.ruoyi.modules.invest.plan.budget.mapper;

import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetCheckLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投资预算校验记录Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface InvestBudgetCheckLogMapper {

    InvestBudgetCheckLog selectInvestBudgetCheckLogById(Long logId);

    List<InvestBudgetCheckLog> selectInvestBudgetCheckLogList(InvestBudgetCheckLog log);

    List<InvestBudgetCheckLog> selectLogsByBudgetId(Long budgetId);

    int insertInvestBudgetCheckLog(InvestBudgetCheckLog log);
}
