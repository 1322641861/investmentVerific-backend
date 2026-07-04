package com.ruoyi.modules.invest.plan.budget.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 投资预算主表 invest_budget
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBudget extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long budgetId;
    private String budgetCode;
    private String budgetName;
    private Long schemeId;
    private String schemeCode;
    private String schemeName;
    private Integer planYear;
    private BigDecimal totalBudget;
    private BigDecimal allocatedAmount;
    private BigDecimal remainingAmount;
    private String budgetStatus;
    private Integer versionNo;
    private String lastCheckStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastCheckTime;
    private String processInstanceId;
    private String delFlag;

    /** 预算明细列表 */
    private List<InvestBudgetItem> itemList;
}
