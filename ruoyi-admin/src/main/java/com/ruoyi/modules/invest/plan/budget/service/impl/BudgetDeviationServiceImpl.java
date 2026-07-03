package com.ruoyi.modules.invest.plan.budget.service.impl;

import com.ruoyi.modules.invest.plan.budget.domain.dto.*;
import com.ruoyi.modules.invest.plan.budget.mapper.BudgetDeviationMapper;
import com.ruoyi.modules.invest.plan.budget.service.IBudgetDeviationService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算执行偏差分析Service实现
 *
 * @author investvf
 */
@Service
public class BudgetDeviationServiceImpl implements IBudgetDeviationService {

    @Autowired
    private BudgetDeviationMapper deviationMapper;

    @Override
    public List<BudgetDeviationVO> selectDeviationList(BudgetDeviationQuery query) {
        List<BudgetDeviationVO> list = deviationMapper.selectDeviationList(query);
        // 计算执行率字段（SQL中的deviation_rate实际是执行率计算值，需要分别计算偏差率和执行率）
        for (BudgetDeviationVO vo : list) {
            calcRates(vo);
        }
        return list;
    }

    @Override
    public List<ProjectDeviationVO> selectProjectDeviation(Long budgetId) {
        List<ProjectDeviationVO> list = deviationMapper.selectProjectDeviation(budgetId);
        for (ProjectDeviationVO vo : list) {
            // 确保偏差率计算
            if (vo.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal devRate = vo.getDeviationAmount()
                        .multiply(new BigDecimal("100"))
                        .divide(vo.getAllocatedAmount(), 2, RoundingMode.HALF_UP);
                vo.setDeviationRate(devRate);

                BigDecimal execRate = vo.getPaidAmount()
                        .multiply(new BigDecimal("100"))
                        .divide(vo.getAllocatedAmount(), 2, RoundingMode.HALF_UP);
                vo.setExecutionRate(execRate);
            }
        }
        return list;
    }

    @Override
    public List<MonthlyTrendVO> selectMonthlyTrend(Long budgetId, Integer year) {
        // 构建全年月度范围
        String startDate = year + "-01-01";
        String endDate = year + "-12-31";

        List<MonthlyTrendVO> actualList = deviationMapper.selectMonthlyTrend(budgetId, startDate, endDate);

        // 生成12个月的完整序列，填充没有数据的月份
        List<MonthlyTrendVO> fullYearList = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            String monthKey = String.format("%d-%02d", year, m);
            MonthlyTrendVO vo = new MonthlyTrendVO();
            vo.setMonth(monthKey);

            // 查找是否有实际数据
            for (MonthlyTrendVO actual : actualList) {
                if (monthKey.equals(actual.getMonth())) {
                    vo.setActualAmount(actual.getActualAmount());
                    break;
                }
            }

            // 预算金额按月均分配（从偏差主表获取totalBudget/12）
            if (m == 1) {
                BudgetDeviationVO summary = deviationMapper.selectDeviationSummary(budgetId);
                if (summary != null && summary.getTotalBudget().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal monthlyBudget = summary.getTotalBudget()
                            .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
                    // 给所有月份设置
                    for (int i = 1; i <= 12; i++) {
                        // 这里不重复设，后续复用
                    }
                    vo.setBudgetAmount(monthlyBudget);
                }
            } else {
                // 复用第一个月计算的月均预算 - 从已有数据复制
                if (!fullYearList.isEmpty()) {
                    vo.setBudgetAmount(fullYearList.get(0).getBudgetAmount());
                }
            }

            // 计算执行率
            if (vo.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rate = vo.getActualAmount()
                        .multiply(new BigDecimal("100"))
                        .divide(vo.getBudgetAmount(), 2, RoundingMode.HALF_UP);
                vo.setExecutionRate(rate);
            }

            fullYearList.add(vo);
        }

        return fullYearList;
    }

    @Override
    public BudgetDeviationVO selectDeviationSummary(Long budgetId) {
        BudgetDeviationVO vo = deviationMapper.selectDeviationSummary(budgetId);
        if (vo != null) {
            calcRates(vo);
        }
        return vo;
    }

    @Override
    public List<BudgetDeviationVO> selectDeviationSummaryByYear(Integer planYear) {
        List<BudgetDeviationVO> list = deviationMapper.selectDeviationSummaryByYear(planYear);
        for (BudgetDeviationVO vo : list) {
            calcRates(vo);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> selectDeviationByDept() {
        return deviationMapper.selectDeviationByDept();
    }

    /**
     * 计算偏差率和执行率
     * 偏差率 = deviationAmount / allocatedAmount * 100
     * 执行率 = actualPaid / allocatedAmount * 100
     */
    private void calcRates(BudgetDeviationVO vo) {
        if (vo.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 偏差率
            BigDecimal devRate = vo.getDeviationAmount()
                    .multiply(new BigDecimal("100"))
                    .divide(vo.getAllocatedAmount(), 2, RoundingMode.HALF_UP);
            vo.setDeviationRate(devRate);

            // 执行率
            BigDecimal execRate = vo.getActualPaid()
                    .multiply(new BigDecimal("100"))
                    .divide(vo.getAllocatedAmount(), 2, RoundingMode.HALF_UP);
            vo.setExecutionRate(execRate);
        }
    }
}
