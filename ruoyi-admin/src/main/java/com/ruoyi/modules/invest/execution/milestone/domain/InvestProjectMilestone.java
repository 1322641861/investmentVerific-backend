package com.ruoyi.modules.invest.execution.milestone.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestProjectMilestone extends BaseEntity {
    private Long milestoneId; private Long projectId; private String projectCode; private String projectName; private Long schemeId; private Integer planYear; private Long parentId; private String milestoneCode; private String milestoneName; private String stageType; @JsonFormat(pattern = "yyyy-MM-dd") private Date planStartDate; @JsonFormat(pattern = "yyyy-MM-dd") private Date planEndDate; @JsonFormat(pattern = "yyyy-MM-dd") private Date actualStartDate; @JsonFormat(pattern = "yyyy-MM-dd") private Date actualEndDate; private BigDecimal weight; private BigDecimal planAmount; private BigDecimal progressRate; private String status; private Long ownerId; private String ownerName; private Integer orderNum; private String delFlag;
    private String acceptanceStandard; private String acceptanceStatus; private String acceptanceBy; @JsonFormat(pattern = "yyyy-MM-dd") private Date acceptanceTime; private String acceptanceOpinion;
}
