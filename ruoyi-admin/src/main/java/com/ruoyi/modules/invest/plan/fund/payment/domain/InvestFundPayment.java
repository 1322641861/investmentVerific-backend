package com.ruoyi.modules.invest.plan.fund.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 资金支付申请 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundPayment extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long paymentId;
    private String paymentCode;
    private Long planId;
    private Long projectId;
    private String projectName;
    private BigDecimal applyAmount;
    private BigDecimal paidAmount;
    private String payeeName;
    private String payeeBank;
    private String payeeAccount;
    private String purpose;
    private String attachment;
    private String payStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    private String processInstanceId;
    private List<InvestFundPaymentSplit> splitList;
}
