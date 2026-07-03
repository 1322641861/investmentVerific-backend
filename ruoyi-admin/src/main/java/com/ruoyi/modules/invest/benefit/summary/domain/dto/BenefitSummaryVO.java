package com.ruoyi.modules.invest.benefit.summary.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BenefitSummaryVO { private Long projectId; private Long schemeId; private BigDecimal roi; private BigDecimal score; private BigDecimal actualRoi; private BigDecimal actualScore; private String summary; }
