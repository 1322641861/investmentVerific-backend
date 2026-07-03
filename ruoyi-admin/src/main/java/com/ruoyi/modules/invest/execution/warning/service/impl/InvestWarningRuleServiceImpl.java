package com.ruoyi.modules.invest.execution.warning.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.execution.warning.domain.InvestWarningRule;
import com.ruoyi.modules.invest.execution.warning.mapper.InvestWarningRuleMapper;
import com.ruoyi.modules.invest.execution.warning.service.IInvestWarningRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警规则配置Service实现
 *
 * @author investvf
 */
@Service
public class InvestWarningRuleServiceImpl implements IInvestWarningRuleService {

    @Autowired
    private InvestWarningRuleMapper mapper;

    @Override
    public InvestWarningRule selectById(Long ruleId) {
        return mapper.selectById(ruleId);
    }

    @Override
    public List<InvestWarningRule> selectList(InvestWarningRule rule) {
        return mapper.selectList(rule);
    }

    @Override
    public List<InvestWarningRule> selectEnabledRules() {
        return mapper.selectEnabledRules();
    }

    @Override
    public int insert(InvestWarningRule rule) {
        rule.setCreateBy(ShiroUtils.getLoginName());
        rule.setCreateTime(DateUtils.getNowDate());
        if (rule.getEnabled() == null) rule.setEnabled("1");
        return mapper.insert(rule);
    }

    @Override
    public int update(InvestWarningRule rule) {
        rule.setUpdateBy(ShiroUtils.getLoginName());
        rule.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(rule);
    }

    @Override
    public int deleteByIds(Long[] ruleIds) {
        return mapper.deleteByIds(ruleIds);
    }

    @Override
    public int toggleEnabled(Long ruleId, String enabled) {
        InvestWarningRule rule = new InvestWarningRule();
        rule.setRuleId(ruleId);
        rule.setEnabled(enabled);
        rule.setUpdateBy(ShiroUtils.getLoginName());
        rule.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(rule);
    }
}
