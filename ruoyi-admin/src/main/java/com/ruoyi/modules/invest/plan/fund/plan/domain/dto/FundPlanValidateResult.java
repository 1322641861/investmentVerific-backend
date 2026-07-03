package com.ruoyi.modules.invest.plan.fund.plan.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** 资金配套计划校验结果 */
@Data
public class FundPlanValidateResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private String checkResult;
    private BigDecimal budgetAmount = BigDecimal.ZERO;
    private BigDecimal plannedTotal = BigDecimal.ZERO;
    private BigDecimal arrivedTotal = BigDecimal.ZERO;
    private BigDecimal paidTotal = BigDecimal.ZERO;
    private BigDecimal matchRate = BigDecimal.ZERO;
    private BigDecimal arrivedRate = BigDecimal.ZERO;
    private BigDecimal paidRate = BigDecimal.ZERO;
    private List<FundItemError> errors = new ArrayList<>();

    public boolean passed() {
        return "0".equals(checkResult);
    }
}
