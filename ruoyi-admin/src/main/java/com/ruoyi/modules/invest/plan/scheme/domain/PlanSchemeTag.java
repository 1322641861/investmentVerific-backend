package com.ruoyi.modules.invest.plan.scheme.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规划方案标签表 plan_scheme_tag
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanSchemeTag extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Long tagId;

    /** 标签名称 */
    private String tagName;

    /** 标签颜色 */
    private String tagColor;

    /** 显示顺序 */
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    private String status;
}
