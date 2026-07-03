package com.ruoyi.modules.invest.plan.fund.check.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 资金配套校验记录 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundCheckLog extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long logId;
    private Long planId;
    private Long projectId;
    private BigDecimal budgetAmount;
    private BigDecimal plannedTotal;
    private BigDecimal arrivedTotal;
    private BigDecimal paidTotal;
    private BigDecimal matchRate;
    private BigDecimal arrivedRate;
    private BigDecimal paidRate;
    private String checkResult;
    private String checkType;
    private String errorJson;
}
