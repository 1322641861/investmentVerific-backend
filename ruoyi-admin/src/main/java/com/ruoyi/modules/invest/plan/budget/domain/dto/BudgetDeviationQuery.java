package com.ruoyi.modules.invest.plan.budget.domain.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预算执行偏差查询参数
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetDeviationQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 规划年度 */
    private Integer planYear;

    /** 方案ID */
    private Long schemeId;

    /** 预算ID（查询单个预算偏差明细时使用） */
    private Long budgetId;

    /** 预算编号或名称（模糊搜索） */
    private String keyword;
}
