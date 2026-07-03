package com.ruoyi.modules.invest.execution.report.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestProgressReport extends BaseEntity {
    private Long reportId; private String reportCode; private Long projectId; private String projectName; private Long milestoneId; private String reportType; private String reportPeriod; private BigDecimal progressRate; private BigDecimal actualAmount; private BigDecimal planAmount; private String reportStatus; private Long reviewerId; private String reviewerName; private Date reviewTime; private String reviewOpinion; private String attachments; private String description; private Long reporterId; private String reporterName; private Date reportTime; private String processInstanceId; private String delFlag;
}
