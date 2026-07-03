package com.ruoyi.modules.invest.plan.budget.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 月度执行趋势VO
 *
 * @author investvf
 */
@Data
public class MonthlyTrendVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 月份 (YYYY-MM) */
    private String month;

    /** 当月预算金额（按月度平均分配） */
    private BigDecimal budgetAmount = BigDecimal.ZERO;

    /** 当月实际支付金额 */
    private BigDecimal actualAmount = BigDecimal.ZERO;

    /** 当月执行率(%) */
    private BigDecimal executionRate = BigDecimal.ZERO;
}
