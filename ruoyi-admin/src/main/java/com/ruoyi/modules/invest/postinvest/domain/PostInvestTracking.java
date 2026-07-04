package com.ruoyi.modules.invest.postinvest.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投后跟踪记录表
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostInvestTracking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 跟踪ID
     */
    private Long trackingId;

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 跟踪周期（季度Q1/Q2/Q3/Q4 年度）
     */
    private String trackingPeriod;

    /**
     * 跟踪年份
     */
    private Integer trackingYear;

    /**
     * 营业收入
     */
    private BigDecimal revenue;

    /**
     * 净利润
     */
    private BigDecimal netProfit;

    /**
     * 净资产
     */
    private BigDecimal netAsset;

    /**
     * 资产负债率
     */
    private BigDecimal debtRatio;

    /**
     * 成长性指标
     */
    private BigDecimal growthRate;

    /**
     * 风险预警等级（绿色正常 黄色预警 红色风险）
     */
    private String riskLevel;

    /**
     * 异常说明
     */
    private String riskNote;

    /**
     * 跟踪报告内容
     */
    private String reportContent;

    /**
     * 附件
     */
    private String attachments;

    /**
     * 跟踪状态（0待提交 1已提交 2已审核）
     */
    private String trackingStatus;

    /**
     * 跟踪人ID
     */
    private Long trackerId;

    /**
     * 跟踪人名称
     */
    private String trackerName;

    /**
     * 跟踪日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trackingDate;

    /**
     * 删除标志
     */
    private String delFlag;
}
