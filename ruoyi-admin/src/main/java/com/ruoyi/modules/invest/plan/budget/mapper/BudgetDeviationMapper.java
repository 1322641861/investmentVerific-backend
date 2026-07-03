package com.ruoyi.modules.invest.plan.budget.mapper;

import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationQuery;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.MonthlyTrendVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.ProjectDeviationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算执行偏差分析Mapper
 *
 * @author investvf
 */
@Mapper
public interface BudgetDeviationMapper {

    /**
     * 查询预算执行偏差汇总列表
     */
    List<BudgetDeviationVO> selectDeviationList(BudgetDeviationQuery query);

    /**
     * 查询单个预算的项目级偏差明细
     */
    List<ProjectDeviationVO> selectProjectDeviation(@Param("budgetId") Long budgetId);

    /**
     * 查询月度执行趋势
     */
    List<MonthlyTrendVO> selectMonthlyTrend(@Param("budgetId") Long budgetId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate);

    /**
     * 查询单个预算的汇总偏差
     */
    BudgetDeviationVO selectDeviationSummary(@Param("budgetId") Long budgetId);

    /**
     * 按年度统计所有预算的汇总偏差
     */
    List<BudgetDeviationVO> selectDeviationSummaryByYear(@Param("planYear") Integer planYear);

    List<java.util.Map<String, Object>> selectDeviationByDept();
}
