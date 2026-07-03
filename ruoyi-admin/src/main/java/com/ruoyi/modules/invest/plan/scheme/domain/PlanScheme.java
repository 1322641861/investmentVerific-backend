package com.ruoyi.modules.invest.plan.scheme.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 规划方案主表 plan_scheme
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanScheme extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 方案ID */
    private Long schemeId;

    /** 方案编号 */
    private String schemeCode;

    /** 方案名称 */
    private String schemeName;

    /** 方案类型（1年度投资规划 2专项投资规划 3战略投资规划 4其他） */
    private String schemeType;

    /** 规划年度 */
    private Integer planYear;

    /** 牵头部门ID */
    private Long leadDeptId;

    /** 牵头部门名称 */
    private String leadDeptName;

    /** 负责人ID */
    private Long leaderId;

    /** 负责人名称 */
    private String leaderName;

    /** 规划总投资额（万元） */
    private BigDecimal totalAmount;

    /** 方案状态（0草稿 1审批中 2已审批 3已驳回 4已终止） */
    private String schemeStatus;

    /** 当前版本号 */
    private Integer versionNo;

    /** 流程实例ID */
    private String processInstanceId;

    /** 附件地址 */
    private String attachments;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 标签ID列表，用于前端绑定 */
    private Long[] tagIds;

    /** 标签列表 */
    private List<PlanSchemeTag> tagList;
}
