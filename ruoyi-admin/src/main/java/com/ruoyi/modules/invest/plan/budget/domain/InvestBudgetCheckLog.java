package com.ruoyi.modules.invest.plan.budget.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 投资预算校验记录表 invest_budget_check_log
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBudgetCheckLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long logId;
    private Long budgetId;
    private Long schemeId;
    private BigDecimal totalBudget;
    private BigDecimal allocatedSum;
    private BigDecimal proposedSum;
    private String checkResult;
    private String checkType;
    private String errorJson;
}
