package com.ruoyi.modules.invest.plan.budget.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预算校验错误项
 *
 * @author investvf
 */
@Data
public class BudgetItemError implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long projectId;
    private String projectName;
    private String rule;
    private BigDecimal expectedAmount;
    private BigDecimal actualAmount;
    private String message;
}
