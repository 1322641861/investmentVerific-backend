package com.ruoyi.modules.invest.plan.budget.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudget;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetCheckLog;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetItemError;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetValidateResult;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetCheckLogMapper;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetItemMapper;
import com.ruoyi.modules.invest.plan.budget.mapper.InvestBudgetMapper;
import com.ruoyi.modules.invest.plan.budget.service.IInvestBudgetService;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeService;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import com.ruoyi.modules.invest.workflow.BpmnProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资预算Service业务层处理
 *
 * @author investvf
 */
@Service
public class InvestBudgetServiceImpl implements IInvestBudgetService {

    @Autowired
    private InvestBudgetMapper budgetMapper;

    @Autowired
    private InvestBudgetItemMapper itemMapper;

    @Autowired
    private InvestBudgetCheckLogMapper checkLogMapper;

    @Autowired
    private IPlanSchemeService planSchemeService;

    @Autowired
    private InvestProjectMapper investProjectMapper;

    @Autowired
    private BpmnProcessService bpmnProcessService;

    @Override
    public InvestBudget selectInvestBudgetById(Long budgetId) {
        InvestBudget budget = budgetMapper.selectInvestBudgetById(budgetId);
        if (budget != null) {
            budget.setItemList(itemMapper.selectItemsByBudgetId(budgetId));
        }
        return budget;
    }

    @Override
    public InvestBudget selectInvestBudgetBySchemeId(Long schemeId) {
        return budgetMapper.selectInvestBudgetBySchemeId(schemeId);
    }

    @Override
    public List<InvestBudget> selectInvestBudgetList(InvestBudget budget) {
        return budgetMapper.selectInvestBudgetList(budget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInvestBudget(InvestBudget budget) {
        fillSchemeInfo(budget);
        budget.setCreateBy(ShiroUtils.getLoginName());
        budget.setCreateTime(DateUtils.getNowDate());
        budget.setDelFlag("0");
        if (budget.getBudgetStatus() == null) {
            budget.setBudgetStatus("0");
        }
        if (budget.getVersionNo() == null) {
            budget.setVersionNo(1);
        }
        if (budget.getAllocatedAmount() == null) {
            budget.setAllocatedAmount(BigDecimal.ZERO);
        }
        if (budget.getTotalBudget() == null) {
            budget.setTotalBudget(BigDecimal.ZERO);
        }
        budget.setRemainingAmount(budget.getTotalBudget().subtract(budget.getAllocatedAmount()));
        if (budget.getBudgetCode() == null || budget.getBudgetCode().isEmpty()) {
            budget.setBudgetCode(generateBudgetCode(budget));
        }
        if (budget.getBudgetName() == null || budget.getBudgetName().isEmpty()) {
            budget.setBudgetName(budget.getSchemeName() + "预算");
        }
        return budgetMapper.insertInvestBudget(budget);
    }

    @Override
    public int updateInvestBudget(InvestBudget budget) {
        fillSchemeInfo(budget);
        budget.setUpdateBy(ShiroUtils.getLoginName());
        budget.setUpdateTime(DateUtils.getNowDate());
        if (budget.getTotalBudget() != null && budget.getAllocatedAmount() != null) {
            budget.setRemainingAmount(budget.getTotalBudget().subtract(budget.getAllocatedAmount()));
        }
        return budgetMapper.updateInvestBudget(budget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteInvestBudgetByIds(Long[] budgetIds) {
        for (Long budgetId : budgetIds) {
            itemMapper.deleteByBudgetId(budgetId);
        }
        return budgetMapper.deleteInvestBudgetByIds(budgetIds);
    }

    @Override
    public List<InvestBudgetItem> selectItemsByBudgetId(Long budgetId) {
        return itemMapper.selectItemsByBudgetId(budgetId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveBudgetItems(Long budgetId, List<InvestBudgetItem> items) {
        InvestBudget budget = budgetMapper.selectInvestBudgetById(budgetId);
        if (budget == null) {
            return 0;
        }
        if (!"0".equals(budget.getBudgetStatus()) && !"3".equals(budget.getBudgetStatus())) {
            return 0;
        }
        itemMapper.deleteByBudgetId(budgetId);
        BigDecimal allocatedSum = BigDecimal.ZERO;
        int rows = 0;
        if (items != null) {
            int orderNum = 1;
            for (InvestBudgetItem item : items) {
                item.setBudgetId(budgetId);
                item.setSchemeId(budget.getSchemeId());
                item.setOrderNum(item.getOrderNum() == null ? orderNum++ : item.getOrderNum());
                item.setCreateBy(ShiroUtils.getLoginName());
                item.setCreateTime(DateUtils.getNowDate());
                fillProjectInfo(item);
                if (item.getAllocatedAmount() == null) {
                    item.setAllocatedAmount(BigDecimal.ZERO);
                }
                allocatedSum = allocatedSum.add(item.getAllocatedAmount());
                rows += itemMapper.insertInvestBudgetItem(item);
                if (item.getProjectId() != null) {
                    InvestProject project = new InvestProject();
                    project.setProjectId(item.getProjectId());
                    project.setBudgetAmount(item.getAllocatedAmount());
                    project.setUpdateBy(ShiroUtils.getLoginName());
                    project.setUpdateTime(DateUtils.getNowDate());
                    investProjectMapper.updateInvestProject(project);
                }
            }
        }
        budget.setAllocatedAmount(allocatedSum);
        budget.setRemainingAmount(budget.getTotalBudget().subtract(allocatedSum));
        budget.setUpdateBy(ShiroUtils.getLoginName());
        budget.setUpdateTime(DateUtils.getNowDate());
        budgetMapper.updateBudgetAmounts(budget);
        return rows;
    }

    @Override
    public List<InvestProject> selectCandidateProjects(Long budgetId) {
        InvestBudget budget = budgetMapper.selectInvestBudgetById(budgetId);
        if (budget == null) {
            return java.util.Collections.emptyList();
        }
        return investProjectMapper.selectInvestProjectBySchemeId(budget.getSchemeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetValidateResult validateBudget(Long budgetId, String checkType) {
        InvestBudget budget = selectInvestBudgetById(budgetId);
        BudgetValidateResult result = new BudgetValidateResult();
        if (budget == null) {
            result.setCheckResult("1");
            BudgetItemError error = new BudgetItemError();
            error.setRule("BUDGET_NOT_FOUND");
            error.setMessage("预算不存在");
            result.getErrors().add(error);
            return result;
        }
        List<InvestBudgetItem> items = budget.getItemList();
        BigDecimal allocatedSum = BigDecimal.ZERO;
        BigDecimal proposedSum = BigDecimal.ZERO;
        for (InvestBudgetItem item : items) {
            BigDecimal allocated = item.getAllocatedAmount() == null ? BigDecimal.ZERO : item.getAllocatedAmount();
            BigDecimal proposed = item.getProposedAmount() == null ? BigDecimal.ZERO : item.getProposedAmount();
            allocatedSum = allocatedSum.add(allocated);
            proposedSum = proposedSum.add(proposed);
            if ("1".equals(item.getItemType()) && item.getProjectId() != null) {
                InvestProject project = investProjectMapper.selectInvestProjectById(item.getProjectId());
                if (project == null || !budget.getSchemeId().equals(project.getSchemeId())) {
                    result.getErrors().add(error(item, "PROJECT_NOT_IN_SCHEME", proposed, allocated, "项目不属于当前规划方案"));
                }
                if (allocated.compareTo(proposed) < 0) {
                    result.getErrors().add(error(item, "BELOW_PROPOSED", proposed, allocated, "分配预算小于项目拟投资金额"));
                }
            }
            if (allocated.compareTo(BigDecimal.ZERO) < 0) {
                result.getErrors().add(error(item, "NEGATIVE_AMOUNT", BigDecimal.ZERO, allocated, "分配预算不能为负数"));
            }
        }
        if (allocatedSum.compareTo(budget.getTotalBudget()) > 0) {
            BudgetItemError totalError = new BudgetItemError();
            totalError.setRule("OVER_TOTAL_BUDGET");
            totalError.setExpectedAmount(budget.getTotalBudget());
            totalError.setActualAmount(allocatedSum);
            totalError.setMessage("明细合计超过预算总额");
            result.getErrors().add(totalError);
        }
        result.setTotalBudget(budget.getTotalBudget());
        result.setAllocatedSum(allocatedSum);
        result.setProposedSum(proposedSum);
        result.setCheckResult(result.getErrors().isEmpty() ? "0" : "1");

        InvestBudgetCheckLog log = new InvestBudgetCheckLog();
        log.setBudgetId(budgetId);
        log.setSchemeId(budget.getSchemeId());
        log.setTotalBudget(result.getTotalBudget());
        log.setAllocatedSum(result.getAllocatedSum());
        log.setProposedSum(result.getProposedSum());
        log.setCheckResult(result.getCheckResult());
        log.setCheckType(checkType == null ? "1" : checkType);
        log.setErrorJson(JSON.toJSONString(result.getErrors()));
        log.setCreateBy(ShiroUtils.getLoginName());
        log.setCreateTime(DateUtils.getNowDate());
        checkLogMapper.insertInvestBudgetCheckLog(log);

        budget.setLastCheckStatus(result.getCheckResult());
        budget.setLastCheckTime(DateUtils.getNowDate());
        budget.setUpdateBy(ShiroUtils.getLoginName());
        budget.setUpdateTime(DateUtils.getNowDate());
        budgetMapper.updateBudgetCheckStatus(budget);
        return result;
    }

    @Override
    public List<InvestBudgetCheckLog> selectCheckLogsByBudgetId(Long budgetId) {
        return checkLogMapper.selectLogsByBudgetId(budgetId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitBudget(Long budgetId) {
        BudgetValidateResult result = validateBudget(budgetId, "2");
        if (!result.passed()) {
            return false;
        }
        InvestBudget budget = budgetMapper.selectInvestBudgetById(budgetId);
        if (budget == null) {
            return false;
        }
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("budgetId", budgetId);
            variables.put("budgetName", budget.getBudgetName());
            variables.put("schemeId", budget.getSchemeId());
            variables.put("schemeName", budget.getSchemeName());
            ProcessInstance processInstance = bpmnProcessService.startProcess("budget_approval", budgetId.toString(), variables);
            budget.setProcessInstanceId(processInstance.getId());
        } catch (Exception e) {
            budget.setProcessInstanceId(null);
        }
        budget.setBudgetStatus("1");
        budget.setUpdateBy(ShiroUtils.getLoginName());
        budget.setUpdateTime(DateUtils.getNowDate());
        return budgetMapper.updateInvestBudget(budget) > 0;
    }

    private void fillSchemeInfo(InvestBudget budget) {
        if (budget == null || budget.getSchemeId() == null) {
            return;
        }
        PlanScheme scheme = planSchemeService.selectPlanSchemeById(budget.getSchemeId());
        if (scheme != null) {
            budget.setSchemeCode(scheme.getSchemeCode());
            budget.setSchemeName(scheme.getSchemeName());
            budget.setPlanYear(scheme.getPlanYear());
        }
    }

    private void fillProjectInfo(InvestBudgetItem item) {
        if (item == null || item.getProjectId() == null) {
            if (item != null && item.getItemType() == null) {
                item.setItemType("2");
            }
            return;
        }
        InvestProject project = investProjectMapper.selectInvestProjectById(item.getProjectId());
        if (project != null) {
            item.setProjectCode(project.getProjectCode());
            item.setProjectName(project.getProjectName());
            item.setProposedAmount(project.getProposedAmount());
            item.setItemType("1");
        }
    }

    private BudgetItemError error(InvestBudgetItem item, String rule, BigDecimal expected, BigDecimal actual, String message) {
        BudgetItemError error = new BudgetItemError();
        error.setProjectId(item.getProjectId());
        error.setProjectName(item.getProjectName());
        error.setRule(rule);
        error.setExpectedAmount(expected);
        error.setActualAmount(actual);
        error.setMessage(message);
        return error;
    }

    private String generateBudgetCode(InvestBudget budget) {
        Integer year = budget.getPlanYear() == null ? java.time.LocalDate.now().getYear() : budget.getPlanYear();
        return "BDG-" + year + "-" + System.currentTimeMillis();
    }
}
