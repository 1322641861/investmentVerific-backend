package com.ruoyi.modules.invest.execution.warning.mapper;

import com.ruoyi.modules.invest.execution.warning.domain.InvestWarningRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预警规则配置Mapper
 *
 * @author investvf
 */
@Mapper
public interface InvestWarningRuleMapper {

    InvestWarningRule selectById(Long ruleId);

    List<InvestWarningRule> selectList(InvestWarningRule rule);

    List<InvestWarningRule> selectEnabledRules();

    int insert(InvestWarningRule rule);

    int update(InvestWarningRule rule);

    int deleteByIds(Long[] ruleIds);
}
