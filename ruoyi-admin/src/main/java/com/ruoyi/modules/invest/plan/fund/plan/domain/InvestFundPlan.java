package com.ruoyi.modules.invest.plan.fund.plan.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 资金配套计划主表 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long planId;
    private String planCode;
    private Long projectId;
    private String projectCode;
    private String projectName;
    private Long schemeId;
    private Long budgetId;
    private Long budgetItemId;
    private BigDecimal budgetAmount;
    private BigDecimal planTotal;
    private BigDecimal arrivedTotal;
    private BigDecimal paidTotal;
    private BigDecimal matchRate;
    private String planStatus;
    private String lastCheckStatus;
    private Date lastCheckTime;
    private String processInstanceId;
    private Integer versionNo;
    private String delFlag;
    private List<InvestFundPlanItem> itemList;
}
