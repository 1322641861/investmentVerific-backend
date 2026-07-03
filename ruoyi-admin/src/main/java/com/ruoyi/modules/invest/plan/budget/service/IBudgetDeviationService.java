package com.ruoyi.modules.invest.plan.budget.service;

import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationQuery;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.MonthlyTrendVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.ProjectDeviationVO;

import java.util.List;
import java.util.Map;

/**
 * 预算执行偏差分析Service接口
 *
 * @author investvf
 */
public interface IBudgetDeviationService {

    /**
     * 查询预算执行偏差汇总列表（分页）
     */
    List<BudgetDeviationVO> selectDeviationList(BudgetDeviationQuery query);

    /**
     * 查询单个预算的项目级偏差明细
     */
    List<ProjectDeviationVO> selectProjectDeviation(Long budgetId);

    /**
     * 查询月度执行趋势
     *
     * @param budgetId 预算ID
     * @param year     年度
     */
    List<MonthlyTrendVO> selectMonthlyTrend(Long budgetId, Integer year);

    /**
     * 查询单个预算的汇总偏差
     */
    BudgetDeviationVO selectDeviationSummary(Long budgetId);

    /**
     * 按年度统计汇总偏差
     */
    List<BudgetDeviationVO> selectDeviationSummaryByYear(Integer planYear);

    List<Map<String, Object>> selectDeviationByDept();
}
