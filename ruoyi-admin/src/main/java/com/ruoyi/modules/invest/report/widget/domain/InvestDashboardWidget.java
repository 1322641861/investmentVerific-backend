package com.ruoyi.modules.invest.report.widget.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestDashboardWidget extends BaseEntity {
    private Long widgetId;
    private String widgetCode;
    private String widgetName;
    private String widgetType;
    private String title;
    private String icon;
    private String dataKey;
    private Long configId;
    private Integer width;
    private Integer height;
    private Integer positionX;
    private Integer positionY;
    private String color;
    private String displayConfig;
    private String status;
    private Integer orderNum;
    private String delFlag;
}
