package com.ruoyi.modules.invest.plan.fund.payment.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** 资金支付来源拆分 */
@Data
public class InvestFundPaymentSplit implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long splitId;
    private Long paymentId;
    private Long itemId;
    private Long sourceId;
    private String sourceName;
    private BigDecimal splitAmount;
}
