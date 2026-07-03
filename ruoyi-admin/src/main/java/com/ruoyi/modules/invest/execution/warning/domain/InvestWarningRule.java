package com.ruoyi.modules.invest.execution.warning.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 预警规则配置表 invest_warning_rule
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestWarningRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long ruleId;
    private String ruleCode;
    private String ruleName;
    private String warningType;
    private String warningLevel;
    private BigDecimal threshold;
    private String operator;
    private String enabled;
    private String ruleConfig;
    private Integer orderNum;
    private String delFlag;
}
