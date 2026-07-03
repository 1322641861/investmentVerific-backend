package com.ruoyi.modules.invest.plan.budget.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目级预算执行偏差VO
 *
 * @author investvf
 */
@Data
public class ProjectDeviationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long projectId;

    /** 项目名称 */
    private String projectName;

    /** 项目拟投金额（预算基准） */
    private BigDecimal budgetAmount = BigDecimal.ZERO;

    /** 已分配预算 */
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    /** 实际支付金额 */
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /** 偏差金额 = allocatedAmount - paidAmount */
    private BigDecimal deviationAmount = BigDecimal.ZERO;

    /** 偏差率(%) = deviationAmount / allocatedAmount * 100 */
    private BigDecimal deviationRate = BigDecimal.ZERO;

    /** 执行率(%) = paidAmount / allocatedAmount * 100 */
    private BigDecimal executionRate = BigDecimal.ZERO;
}
