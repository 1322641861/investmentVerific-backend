package com.ruoyi.modules.invest.plan.budget.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算校验结果
 *
 * @author investvf
 */
@Data
public class BudgetValidateResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String checkResult;
    private BigDecimal totalBudget = BigDecimal.ZERO;
    private BigDecimal allocatedSum = BigDecimal.ZERO;
    private BigDecimal proposedSum = BigDecimal.ZERO;
    private List<BudgetItemError> errors = new ArrayList<>();

    public boolean passed() {
        return "0".equals(checkResult);
    }
}
