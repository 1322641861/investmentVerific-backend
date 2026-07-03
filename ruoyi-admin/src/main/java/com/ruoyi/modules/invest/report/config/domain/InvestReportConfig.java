package com.ruoyi.modules.invest.report.config.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestReportConfig extends BaseEntity {
    private Long configId;
    private String configCode;
    private String configName;
    private String reportType;
    private String chartType;
    private String dataSource;
    private String querySql;
    private String displayConfig;
    private Integer refreshInterval;
    private String status;
    private Integer orderNum;
    private String delFlag;
}
