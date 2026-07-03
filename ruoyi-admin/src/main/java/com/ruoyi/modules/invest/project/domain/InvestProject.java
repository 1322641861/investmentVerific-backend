package com.ruoyi.modules.invest.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资项目表
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestProject extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 关联规划方案ID
     */
    private Long schemeId;

    /**
     * 关联规划方案编号
     */
    private String schemeCode;

    /**
     * 关联规划方案名称
     */
    private String schemeName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目类型（1股权投资 2债权投资 3夹层投资）
     */
    private String projectType;

    /**
     * 投资行业
     */
    private String industry;

    /**
     * 项目所在地区
     */
    private String region;

    /**
     * 规划年度
     */
    private Integer planYear;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 拟投资金额（万元）
     */
    private BigDecimal proposedAmount;

    /**
     * 已投资金额（万元）
     */
    private BigDecimal investedAmount;

    /**
     * 已分配预算（万元）
     */
    private BigDecimal budgetAmount;

    /**
     * 项目进度（%）
     */
    private BigDecimal progressRate;

    /**
     * 效益评分
     */
    private BigDecimal benefitScore;

    /**
     * 优先级（1高 2中 3低）
     */
    private String priority;

    /**
     * 附件地址
     */
    private String attachments;

    /**
     * 项目状态（0草稿 1审批中 2尽调中 3风险评估中 4已审批 5已投资 6已终止）
     */
    private String projectStatus;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 项目负责人ID
     */
    private Long leaderId;

    /**
     * 项目负责人名称
     */
    private String leaderName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 立项时间
     */
    private Date setupTime;

    /**
     * 预计退出时间
     */
    private Date expectedExitTime;

    /**
     * 实际退出时间
     */
    private Date actualExitTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}
