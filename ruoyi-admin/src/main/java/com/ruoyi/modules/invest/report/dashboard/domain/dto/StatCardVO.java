package com.ruoyi.modules.invest.report.dashboard.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatCardVO {
    private String title;
    private String icon;
    private String color;
    private BigDecimal value;
    private String unit;
    private String trend;
    private BigDecimal trendValue;
}
