package com.ruoyi.modules.invest.benefit.evaluation.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestBenefitEvaluation extends BaseEntity { private Long evaluationId; private String evaluationCode; private Long projectId; private String projectName; private Long schemeId; private Integer evaluationYear; private BigDecimal actualIncome; private BigDecimal actualProfit; private BigDecimal actualRoi; private BigDecimal actualPaybackPeriod; private BigDecimal actualScore; private String deviationNote; private String evaluationStatus; private String delFlag; }
