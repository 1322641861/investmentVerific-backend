package com.ruoyi.modules.invest.duediligence.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 尽职调查表
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DueDiligence extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 尽调ID
     */
    private Long dueDiligenceId;

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 尽调标题
     */
    private String reportTitle;

    /**
     * 尽调内容（JSON格式存储各部分）
     */
    private String dueDiligenceContent;

    /**
     * 财务尽调结论
     */
    private String financeConclusion;

    /**
     * 法务尽调结论
     */
    private String legalConclusion;

    /**
     * 市场尽调结论
     */
    private String marketConclusion;

    /**
     * 尽调总体结论（1推荐投资 2谨慎投资 3不推荐投资）
     */
    private String overallConclusion;

    /**
     * 附件地址（多个逗号分隔）
     */
    private String attachments;

    /**
     * 尽调状态（0草稿 1审批中 2已完成 3已驳回）
     */
    private String ddStatus;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 尽调负责人ID
     */
    private Long leaderId;

    /**
     * 尽调负责人名称
     */
    private String leaderName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 删除标志（0存在 2删除）
     */
    private String delFlag;
}
