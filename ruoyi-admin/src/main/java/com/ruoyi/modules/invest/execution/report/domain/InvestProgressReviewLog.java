package com.ruoyi.modules.invest.execution.report.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class InvestProgressReviewLog implements Serializable {
    private Long logId; private Long reportId; private String action; private Long operatorId; private String operatorName; private String opinion; private Date createTime;
}
