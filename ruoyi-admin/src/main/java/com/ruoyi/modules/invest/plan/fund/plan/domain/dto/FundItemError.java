package com.ruoyi.modules.invest.plan.fund.plan.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** 资金配套校验错误项 */
@Data
public class FundItemError implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rule;
    private Long itemId;
    private String sourceName;
    private BigDecimal expectedAmount;
    private BigDecimal actualAmount;
    private String message;
}
