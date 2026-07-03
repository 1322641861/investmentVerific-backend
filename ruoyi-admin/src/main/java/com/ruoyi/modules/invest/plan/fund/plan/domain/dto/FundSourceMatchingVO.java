package com.ruoyi.modules.invest.plan.fund.plan.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资金来源配套明细VO
 *
 * @author investvf
 */
@Data
public class FundSourceMatchingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long sourceId;
    private String sourceName;
    private String sourceType;
    private String sourceTypeLabel;
    private BigDecimal plannedAmount = BigDecimal.ZERO;
    private BigDecimal arrivedAmount = BigDecimal.ZERO;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private BigDecimal sourceArrivedRate = BigDecimal.ZERO;
}
