package com.ruoyi.modules.invest.execution.warning.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import com.ruoyi.modules.invest.execution.milestone.mapper.InvestProjectMilestoneMapper;
import com.ruoyi.modules.invest.execution.warning.domain.InvestProgressWarning;
import com.ruoyi.modules.invest.execution.warning.domain.InvestWarningRule;
import com.ruoyi.modules.invest.execution.warning.mapper.InvestProgressWarningMapper;
import com.ruoyi.modules.invest.execution.warning.mapper.InvestWarningRuleMapper;
import com.ruoyi.modules.invest.execution.warning.service.IInvestProgressWarningService;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 进度预警Service实现
 * 支持从invest_warning_rule表动态读取预警规则
 *
 * @author investvf
 */
@Service
public class InvestProgressWarningServiceImpl implements IInvestProgressWarningService {

    private static final Logger log = LoggerFactory.getLogger(InvestProgressWarningServiceImpl.class);

    @Autowired
    private InvestProgressWarningMapper mapper;

    @Autowired
    private InvestProjectMilestoneMapper milestoneMapper;

    @Autowired
    private InvestProjectMapper projectMapper;

    @Autowired
    private InvestWarningRuleMapper warningRuleMapper;

    @Override
    public List<InvestProgressWarning> selectList(InvestProgressWarning w) {
        return mapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int scan(Long projectId) {
        List<InvestWarningRule> rules = warningRuleMapper.selectEnabledRules();
        if (rules == null || rules.isEmpty()) {
            log.warn("未找到启用的预警规则，跳过扫描");
            return 0;
        }

        List<InvestProjectMilestone> milestones = milestoneMapper.selectByProjectId(projectId);
        if (milestones == null || milestones.isEmpty()) {
            log.info("项目[{}]无里程碑，跳过扫描", projectId);
            return 0;
        }

        Date now = DateUtils.getNowDate();
        int rows = 0;

        for (InvestProjectMilestone m : milestones) {
            for (InvestWarningRule rule : rules) {
                rows += applyRule(rule, m, now);
            }
        }

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int scanAllProjects() {
        // 查询所有活动项目（状态为进行中、未启动等）
        InvestProject query = new InvestProject();
        query.setDelFlag("0");
        List<InvestProject> projects = projectMapper.selectInvestProjectList(query);

        int total = 0;
        for (InvestProject p : projects) {
            try {
                total += scan(p.getProjectId());
            } catch (Exception e) {
                log.error("扫描项目[{}]预警失败: {}", p.getProjectId(), e.getMessage());
            }
        }
        log.info("全项目预警扫描完成，共生成{}条预警", total);
        return total;
    }

    @Override
    public boolean handle(Long id, String status, String opinion) {
        InvestProgressWarning w = mapper.selectById(id);
        if (w == null) return false;
        w.setStatus(status);
        w.setResolveBy(ShiroUtils.getLoginName());
        w.setResolveOpinion(opinion);
        w.setResolveTime(DateUtils.getNowDate());
        w.setUpdateBy(ShiroUtils.getLoginName());
        w.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(w) > 0;
    }

    /**
     * 根据规则对里程碑执行检查
     */
    private int applyRule(InvestWarningRule rule, InvestProjectMilestone m, Date now) {
        String ruleCode = rule.getRuleCode();

        switch (ruleCode) {
            case "RULE_MILESTONE_OVERDUE":
                return checkMilestoneOverdue(rule, m, now);
            case "RULE_PROGRESS_LAGGING":
                return checkProgressLagging(rule, m, now);
            case "RULE_AMOUNT_DEVIATION":
                return checkAmountDeviation(rule, m, now);
            case "RULE_PAYMENT_COVERAGE":
                return checkPaymentCoverage(rule, m, now);
            default:
                log.debug("未知规则编码: {}", ruleCode);
                return 0;
        }
    }

    /**
     * 里程碑超期检查：计划结束日期已过且状态不是已完成
     */
    private int checkMilestoneOverdue(InvestWarningRule rule, InvestProjectMilestone m, Date now) {
        if (m.getPlanEndDate() == null) return 0;
        if (m.getPlanEndDate().before(now) && !"2".equals(m.getStatus())) {
            if (existsWarning(rule.getRuleCode(), m.getProjectId(), m.getMilestoneId())) return 0;
            return insertWarning(rule, m, "里程碑已延期：" + m.getMilestoneName(), "3",
                    null, null, null);
        }
        // 自动消除：里程碑已完成，关闭已有超期预警
        autoResolve(rule.getRuleCode(), m.getProjectId(), m.getMilestoneId(), "里程碑已按时完成");
        return 0;
    }

    /**
     * 进度落后检查：进度率低于阈值
     */
    private int checkProgressLagging(InvestWarningRule rule, InvestProjectMilestone m, Date now) {
        if (m.getProgressRate() == null) return 0;
        BigDecimal threshold = rule.getThreshold() != null ? rule.getThreshold() : new BigDecimal("50");
        if (m.getProgressRate().compareTo(threshold) < 0 && "1".equals(m.getStatus())) {
            if (existsWarning(rule.getRuleCode(), m.getProjectId(), m.getMilestoneId())) return 0;
            return insertWarning(rule, m, "里程碑进度偏低：" + m.getMilestoneName() + " (" + m.getProgressRate() + "%)",
                    "2", m.getProgressRate(), null, threshold);
        }
        if (m.getProgressRate() != null && m.getProgressRate().compareTo(threshold) >= 0) {
            autoResolve(rule.getRuleCode(), m.getProjectId(), m.getMilestoneId(), "进度已达标");
        }
        return 0;
    }

    /**
     * 金额偏差检查：计划金额与实际支付的偏差（需要资金模块数据支持）
     * 当前检查里程碑计划金额是否异常
     */
    private int checkAmountDeviation(InvestWarningRule rule, InvestProjectMilestone m, Date now) {
        if (m.getPlanAmount() == null || m.getPlanAmount().compareTo(BigDecimal.ZERO) <= 0) return 0;
        // 预留：这里需要从资金模块获取实际支付金额
        // 当前仅做计划金额的合理性检查
        return 0;
    }

    /**
     * 支付覆盖不足检查（预留，需要资金模块数据）
     */
    private int checkPaymentCoverage(InvestWarningRule rule, InvestProjectMilestone m, Date now) {
        // 此规则需要资金模块数据，暂时跳过
        return 0;
    }

    /**
     * 检查是否已存在未处理的相同规则预警（去重）
     */
    private boolean existsWarning(String ruleCode, Long projectId, Long milestoneId) {
        InvestProgressWarning query = new InvestProgressWarning();
        query.setRuleCode(ruleCode);
        // 直接使用mapper的selectList查询去重
        List<InvestProgressWarning> existing = mapper.selectList(query);
        for (InvestProgressWarning w : existing) {
            if (projectId.equals(w.getProjectId())
                    && milestoneId.equals(w.getMilestoneId())
                    && "0".equals(w.getStatus())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 自动消除已满足条件的预警
     */
    private void autoResolve(String ruleCode, Long projectId, Long milestoneId, String opinion) {
        InvestProgressWarning query = new InvestProgressWarning();
        query.setRuleCode(ruleCode);
        List<InvestProgressWarning> existing = mapper.selectList(query);
        Date now = DateUtils.getNowDate();
        for (InvestProgressWarning w : existing) {
            if (projectId.equals(w.getProjectId())
                    && milestoneId.equals(w.getMilestoneId())
                    && "0".equals(w.getStatus())) {
                w.setStatus("2");
                w.setResolveBy("system");
                w.setResolveOpinion(opinion);
                w.setResolveTime(now);
                w.setUpdateBy("system");
                w.setUpdateTime(now);
                mapper.update(w);
                log.info("自动消除预警[{}]: {}", w.getWarningId(), opinion);
            }
        }
    }

    /**
     * 创建预警记录
     */
    private int insertWarning(InvestWarningRule rule, InvestProjectMilestone m,
                              String msg, String level,
                              BigDecimal actual, BigDecimal plan, BigDecimal threshold) {
        InvestProgressWarning w = new InvestProgressWarning();
        w.setProjectId(m.getProjectId());
        w.setMilestoneId(m.getMilestoneId());
        w.setWarningType(rule.getWarningType());
        w.setWarningLevel(level != null ? level : rule.getWarningLevel());
        w.setRuleCode(rule.getRuleCode());
        w.setWarningMsg(msg);
        w.setPlanValue(plan);
        w.setActualValue(actual);
        w.setDeviation(threshold);
        w.setStatus("0");
        w.setTriggerTime(DateUtils.getNowDate());
        w.setCreateBy("system");
        w.setCreateTime(DateUtils.getNowDate());
        return mapper.insert(w);
    }
}
