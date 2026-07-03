package com.ruoyi.modules.invest.plan.scheme.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 规划方案标签关联表 plan_scheme_tag_rel
 *
 * @author investvf
 */
@Data
public class PlanSchemeTagRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long relId;

    /** 方案ID */
    private Long schemeId;

    /** 标签ID */
    private Long tagId;
}
