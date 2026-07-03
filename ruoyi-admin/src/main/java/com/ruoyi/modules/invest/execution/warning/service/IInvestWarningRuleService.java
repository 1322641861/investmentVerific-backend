package com.ruoyi.modules.invest.execution.warning.service;

import com.ruoyi.modules.invest.execution.warning.domain.InvestWarningRule;

import java.util.List;

/**
 * 预警规则配置Service接口
 *
 * @author investvf
 */
public interface IInvestWarningRuleService {

    InvestWarningRule selectById(Long ruleId);

    List<InvestWarningRule> selectList(InvestWarningRule rule);

    List<InvestWarningRule> selectEnabledRules();

    int insert(InvestWarningRule rule);

    int update(InvestWarningRule rule);

    int deleteByIds(Long[] ruleIds);

    int toggleEnabled(Long ruleId, String enabled);
}
