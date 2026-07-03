package com.ruoyi.modules.invest.workflow;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.modules.invest.benefit.calculation.domain.InvestBenefitCalculation;
import com.ruoyi.modules.invest.benefit.calculation.mapper.InvestBenefitCalculationMapper;
import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReport;
import com.ruoyi.modules.invest.execution.report.mapper.InvestProgressReportMapper;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudget;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetMapper;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务流程状态回写服务
 * 在流程结束时，根据流程定义KEY和审批结果，更新对应业务单据的状态
 *
 * @author investvf
 */
@Service
public class BusinessStatusCallbackService {

    private static final Logger log = LoggerFactory.getLogger(BusinessStatusCallbackService.class);

    @Autowired
    private PlanSchemeMapper planSchemeMapper;

    @Autowired
    private InvestBudgetMapper budgetMapper;

    @Autowired
    private InvestProgressReportMapper progressReportMapper;

    @Autowired
    private InvestBenefitCalculationMapper benefitCalcMapper;

    /**
     * 流程定义KEY → 业务类型映射
     */
    public static final String KEY_SCHEME = "plan_scheme_approval";
    public static final String KEY_BUDGET = "budget_approval";
    public static final String KEY_PROGRESS = "progress-approval";
    public static final String KEY_BENEFIT = "benefit_calculation_approval";

    /**
     * 根据流程定义KEY和业务主键，更新业务单据状态
     *
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键（对应业务表的ID）
     * @param approved             是否审批通过（true=通过，false=驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBusinessStatus(String processDefinitionKey, String businessKey, boolean approved) {
        log.info("业务状态回写：流程KEY={}, 业务KEY={}, 审批结果={}", processDefinitionKey, businessKey, approved);

        switch (processDefinitionKey) {
            case KEY_SCHEME:
                updateSchemeStatus(businessKey, approved);
                break;
            case KEY_BUDGET:
                updateBudgetStatus(businessKey, approved);
                break;
            case KEY_PROGRESS:
                updateProgressStatus(businessKey, approved);
                break;
            case KEY_BENEFIT:
                updateBenefitCalcStatus(businessKey, approved);
                break;
            default:
                log.warn("未知的流程定义KEY: {}，不执行状态回写", processDefinitionKey);
        }
    }

    /**
     * 更新规划方案状态
     * schemeStatus: 0草稿 1审批中 2已审批 3已驳回 4已终止
     */
    private void updateSchemeStatus(String businessKey, boolean approved) {
        try {
            Long schemeId = Long.parseLong(businessKey);
            PlanScheme scheme = new PlanScheme();
            scheme.setSchemeId(schemeId);
            scheme.setSchemeStatus(approved ? "2" : "3");
            scheme.setUpdateBy("system");
            scheme.setUpdateTime(DateUtils.getNowDate());
            planSchemeMapper.updateSchemeStatus(scheme);
            log.info("规划方案[{}]状态更新为：{}", schemeId, scheme.getSchemeStatus());
        } catch (NumberFormatException e) {
            log.error("规划方案状态回写失败，businessKey不是有效的ID: {}", businessKey, e);
        }
    }

    /**
     * 更新预算状态
     * budgetStatus: 0草稿 1审批中 2已审批 3已驳回
     */
    private void updateBudgetStatus(String businessKey, boolean approved) {
        try {
            Long budgetId = Long.parseLong(businessKey);
            InvestBudget budget = budgetMapper.selectInvestBudgetById(budgetId);
            if (budget == null) {
                log.warn("预算不存在: {}", budgetId);
                return;
            }
            budget.setBudgetStatus(approved ? "2" : "3");
            budget.setUpdateBy("system");
            budget.setUpdateTime(DateUtils.getNowDate());
            // 使用通用update方法（需要确保mapper的update方法支持status字段）
            budgetMapper.updateInvestBudget(budget);
            log.info("投资预算[{}]状态更新为：{}", budgetId, budget.getBudgetStatus());
        } catch (NumberFormatException e) {
            log.error("预算状态回写失败，businessKey不是有效的ID: {}", businessKey, e);
        }
    }

    /**
     * 更新进度上报状态
     * reportStatus: 0草稿 1已提交 2审核中 3已审批 4已撤回 5已驳回
     */
    private void updateProgressStatus(String businessKey, boolean approved) {
        try {
            Long reportId = Long.parseLong(businessKey);
            InvestProgressReport report = progressReportMapper.selectById(reportId);
            if (report == null) {
                log.warn("进度上报不存在: {}", reportId);
                return;
            }
            report.setReportStatus(approved ? "3" : "5");
            report.setUpdateBy("system");
            report.setUpdateTime(DateUtils.getNowDate());
            progressReportMapper.update(report);
            log.info("进度上报[{}]状态更新为：{}", reportId, report.getReportStatus());
        } catch (NumberFormatException e) {
            log.error("进度上报状态回写失败，businessKey不是有效的ID: {}", businessKey, e);
        }
    }

    /**
     * 更新效益测算状态
     * calcStatus: 0草稿 1审批中 2已审批 3已驳回
     */
    private void updateBenefitCalcStatus(String businessKey, boolean approved) {
        try {
            Long calcId = Long.parseLong(businessKey);
            InvestBenefitCalculation calc = benefitCalcMapper.selectById(calcId);
            if (calc == null) {
                log.warn("效益测算不存在: {}", calcId);
                return;
            }
            calc.setCalcStatus(approved ? "2" : "3");
            calc.setUpdateBy("system");
            calc.setUpdateTime(DateUtils.getNowDate());
            benefitCalcMapper.update(calc);
            log.info("效益测算[{}]状态更新为：{}", calcId, calc.getCalcStatus());
        } catch (NumberFormatException e) {
            log.error("效益测算状态回写失败，businessKey不是有效的ID: {}", businessKey, e);
        }
    }
}
