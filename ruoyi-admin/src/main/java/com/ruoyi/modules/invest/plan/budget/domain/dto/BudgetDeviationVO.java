package com.ruoyi.modules.invest.plan.budget.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预算执行偏差分析结果VO
 *
 * @author investvf
 */
@Data
public class BudgetDeviationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 预算ID */
    private Long budgetId;

    /** 预算编号 */
    private String budgetCode;

    /** 预算名称 */
    private String budgetName;

    /** 规划年度 */
    private Integer planYear;

    /** 方案名称 */
    private String schemeName;

    /** 总预算金额 */
    private BigDecimal totalBudget = BigDecimal.ZERO;

    /** 已分配金额 */
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    /** 实际支付金额（从资金模块汇总） */
    private BigDecimal actualPaid = BigDecimal.ZERO;

    /** 偏差金额 = allocatedAmount - actualPaid */
    private BigDecimal deviationAmount = BigDecimal.ZERO;

    /** 偏差率(%) = deviationAmount / allocatedAmount * 100 */
    private BigDecimal deviationRate = BigDecimal.ZERO;

    /** 执行率(%) = actualPaid / allocatedAmount * 100 */
    private BigDecimal executionRate = BigDecimal.ZERO;

    /** 预算状态 */
    private String budgetStatus;
}
