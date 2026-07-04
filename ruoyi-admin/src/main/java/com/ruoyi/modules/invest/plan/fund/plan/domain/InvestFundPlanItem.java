package com.ruoyi.modules.invest.plan.fund.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/** 资金配套计划明细 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundPlanItem extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long itemId;
    private Long planId;
    private Long sourceId;
    private String sourceCode;
    private String sourceName;
    private String sourceType;
    private BigDecimal plannedAmount;
    private BigDecimal arrivedAmount;
    private BigDecimal paidAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expectArrivalDate;
    private Integer orderNum;
}
