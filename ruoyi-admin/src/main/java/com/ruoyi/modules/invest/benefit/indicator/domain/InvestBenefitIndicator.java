package com.ruoyi.modules.invest.benefit.indicator.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBenefitIndicator extends BaseEntity { private Long indicatorId; private String indicatorCode; private String indicatorName; private String indicatorType; private String unit; private String formula; private BigDecimal standardValue; private BigDecimal weight; private String status; private String delFlag; }
