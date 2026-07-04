package com.ruoyi.modules.invest.plan.fund.plan.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金配套率统计VO
 *
 * @author investvf
 */
@Data
public class FundMatchingStatVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long planId;
    private Long projectId;
    private String projectName;
    private BigDecimal budgetAmount = BigDecimal.ZERO;
    private BigDecimal planTotal = BigDecimal.ZERO;
    private BigDecimal arrivedTotal = BigDecimal.ZERO;
    private BigDecimal paidTotal = BigDecimal.ZERO;
    private BigDecimal matchRate = BigDecimal.ZERO;
    private BigDecimal arrivedRate = BigDecimal.ZERO;
    private BigDecimal paidRate = BigDecimal.ZERO;
    private String planStatus;
    private String planStatusLabel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastCheckTime;
    private String warningLevel; // green/yellow/red
}
