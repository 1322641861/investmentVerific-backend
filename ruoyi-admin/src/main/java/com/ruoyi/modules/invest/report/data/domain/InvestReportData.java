package com.ruoyi.modules.invest.report.data.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InvestReportData {
    private Long dataId;
    private Long configId;
    private LocalDate reportDate;
    private String reportPeriod;
    private String statDimension;
    private String statKey;
    private BigDecimal statValue;
    private String statLabel;
    private String additionalData;
    private LocalDateTime createTime;
}
