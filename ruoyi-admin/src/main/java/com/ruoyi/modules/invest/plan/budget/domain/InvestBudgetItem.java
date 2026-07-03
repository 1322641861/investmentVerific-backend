package com.ruoyi.modules.invest.plan.budget.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 投资预算明细表 invest_budget_item
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBudgetItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long itemId;
    private Long budgetId;
    private Long schemeId;
    private Long projectId;
    private String projectCode;
    private String projectName;
    private BigDecimal proposedAmount;
    private BigDecimal allocatedAmount;
    private String itemType;
    private Integer orderNum;
}
