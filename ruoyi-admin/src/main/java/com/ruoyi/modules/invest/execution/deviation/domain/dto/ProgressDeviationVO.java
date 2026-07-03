package com.ruoyi.modules.invest.execution.deviation.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProgressDeviationVO { private Long projectId; private BigDecimal expectedProgress; private BigDecimal actualProgress; private BigDecimal progressDeviation; private BigDecimal paymentCoverage; private String summary; }
