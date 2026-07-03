package com.ruoyi.modules.invest.benefit.calculation.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBenefitCalculation extends BaseEntity { private Long calcId; private String calcCode; private Long projectId; private String projectCode; private String projectName; private Long schemeId; private String calcType; private Integer calcYear; private BigDecimal investAmount; private BigDecimal expectedIncome; private BigDecimal expectedProfit; private BigDecimal roi; private BigDecimal paybackPeriod; private BigDecimal benefitScore; private String calcStatus; private String processInstanceId; private String delFlag; private List<InvestBenefitCalcItem> itemList; }
