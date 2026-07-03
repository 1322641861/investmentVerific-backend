package com.ruoyi.modules.invest.plan.fund.arrival.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/** 资金到账登记 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundArrival extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long arrivalId;
    private Long planId;
    private Long itemId;
    private Long sourceId;
    private BigDecimal arrivedAmount;
    private Date arrivedDate;
    private String payerName;
    private String voucherNo;
    private String attachment;
    private String arrivalStatus;
}
