package com.ruoyi.modules.invest.report.dashboard.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDataVO {
    private StatCardVO totalProject;
    private StatCardVO totalInvest;
    private StatCardVO inProgress;
    private StatCardVO avgRoi;
    private List<Map<String, Object>> projectStatusChart;
    private List<Map<String, Object>> investTrendChart;
    private List<Map<String, Object>> fundSourceChart;
    private List<Map<String, Object>> recentProjects;
}
