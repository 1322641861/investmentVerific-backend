package com.ruoyi.modules.invest.risk.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险评估表
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAssessment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 风险评估ID
     */
    private Long riskId;

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 评估标题
     */
    private String assessmentTitle;

    /**
     * 市场风险得分（0-100）
     */
    private Integer marketRiskScore;

    /**
     * 财务风险得分（0-100）
     */
    private Integer financeRiskScore;

    /**
     * 技术风险得分（0-100）
     */
    private Integer techRiskScore;

    /**
     * 管理风险得分（0-100）
     */
    private Integer managementRiskScore;

    /**
     * 政策风险得分（0-100）
     */
    private Integer policyRiskScore;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 风险等级（A低风险 B中风险 C高风险 D极高风险）
     */
    private String riskLevel;

    /**
     * 评估结论
     */
    private String conclusion;

    /**
     * 附件地址
     */
    private String attachments;

    /**
     * 评估状态（0草稿 1审批中 2已完成 3已驳回）
     */
    private String assessmentStatus;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 评估人ID
     */
    private Long assessorId;

    /**
     * 评估人名称
     */
    private String assessorName;

    /**
     * 评估时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assessmentTime;

    /**
     * 删除标志
     */
    private String delFlag;
}
