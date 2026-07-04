package com.ruoyi.modules.invest.execution.warning.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestProgressWarning extends BaseEntity {
    private Long warningId; private Long projectId; private Long milestoneId; private String warningType; private String warningLevel; private BigDecimal planValue; private BigDecimal actualValue; private BigDecimal deviation; private String ruleCode; private String warningMsg; private String status; @JsonFormat(pattern = "yyyy-MM-dd") private Date triggerTime; @JsonFormat(pattern = "yyyy-MM-dd") private Date resolveTime; private String resolveBy; private String resolveOpinion;
}
