package com.ruoyi.modules.invest.benefit.calculation.domain;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InvestBenefitCalcItem implements Serializable { private Long itemId; private Long calcId; private Long indicatorId; private String indicatorCode; private String indicatorName; private BigDecimal indicatorValue; private BigDecimal score; private BigDecimal weight; private BigDecimal weightedScore; private String remark; }
