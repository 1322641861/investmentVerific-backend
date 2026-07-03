package com.ruoyi.modules.invest.project.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import com.ruoyi.modules.invest.project.service.IInvestProjectService;
import com.ruoyi.modules.invest.workflow.BpmnProcessService;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资项目Service业务层处理
 *
 * @author investvf
 */
@Service
public class InvestProjectServiceImpl implements IInvestProjectService {

    @Autowired
    private InvestProjectMapper investProjectMapper;

    @Autowired
    private BpmnProcessService bpmnProcessService;

    @Autowired
    private IPlanSchemeService planSchemeService;

    @Override
    public Map<String, Object> validateCompliance(Long projectId) {
        InvestProject project = selectInvestProjectById(projectId);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> errors = new java.util.ArrayList<>();
        result.put("passed", true);
        result.put("errors", errors);

        if (project == null) {
            errors.add(error("project", "项目不存在"));
            result.put("passed", false);
            return result;
        }

        // 1. 检查方案关联
        if (project.getSchemeId() == null) {
            errors.add(error("scheme", "未关联规划方案"));
        } else {
            PlanScheme scheme = planSchemeService.selectPlanSchemeById(project.getSchemeId());
            if (scheme == null) {
                errors.add(error("scheme", "关联的规划方案不存在"));
            } else if (!"2".equals(scheme.getSchemeStatus())) {
                errors.add(error("scheme", "规划方案未审批通过（当前状态：" + scheme.getSchemeStatus() + "）"));
            }
        }

        // 2. 检查必填字段
        if (project.getProjectName() == null || project.getProjectName().isEmpty()) {
            errors.add(error("name", "项目名称不能为空"));
        }
        if (project.getProposedAmount() == null || project.getProposedAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            errors.add(error("amount", "拟投金额必须大于0"));
        }
        if (project.getDeptId() == null) {
            errors.add(error("dept", "未指定负责部门"));
        }

        // 3. 检查预算分配
        if (project.getBudgetAmount() == null || project.getBudgetAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            errors.add(error("budget", "未分配预算或预算金额为0"));
        }

        result.put("passed", errors.isEmpty());
        return result;
    }

    @Override
    public Map<String, Object> transitionStatus(Long projectId, String targetStatus) {
        InvestProject project = selectInvestProjectById(projectId);
        Map<String, Object> result = new HashMap<>();
        if (project == null) {
            result.put("success", false); result.put("message", "项目不存在");
            return result;
        }

        String current = project.getProjectStatus();
        String[][] transitions = {
            {"0", "1", "提交审批"},    // 草稿→审批中
            {"1", "2", "审批通过"},    // 审批中→已审批
            {"1", "3", "审批驳回"},    // 审批中→已驳回
            {"2", "4", "启动项目"},    // 已审批→进行中
            {"4", "5", "完成项目"},    // 进行中→已完成
            {"4", "6", "暂停项目"},    // 进行中→已暂停
            {"6", "4", "恢复项目"},    // 已暂停→进行中
            {"0", "0", "保存草稿"},    // 草稿→草稿(编辑)
            {"3", "0", "重新提报"},    // 已驳回→草稿
        };

        boolean valid = false;
        String action = "";
        for (String[] t : transitions) {
            if (t[0].equals(current) && t[1].equals(targetStatus)) {
                valid = true; action = t[2]; break;
            }
        }

        if (!valid) {
            result.put("success", false);
            result.put("message", "不允许的状态变更：" + statusLabel(current) + " → " + statusLabel(targetStatus));
            return result;
        }

        project.setProjectStatus(targetStatus);
        project.setUpdateBy(ShiroUtils.getLoginName());
        project.setUpdateTime(DateUtils.getNowDate());
        investProjectMapper.updateInvestProject(project);

        result.put("success", true);
        result.put("message", action + "成功");
        result.put("action", action);
        result.put("currentStatus", targetStatus);
        return result;
    }

    private String statusLabel(String status) {
        Map<String, String> labels = new HashMap<>();
        labels.put("0", "草稿"); labels.put("1", "审批中"); labels.put("2", "已审批");
        labels.put("3", "已驳回"); labels.put("4", "进行中"); labels.put("5", "已完成");
        labels.put("6", "已暂停");
        return labels.getOrDefault(status, "未知");
    }

    private Map<String, String> error(String field, String msg) {
        Map<String, String> e = new HashMap<>();
        e.put("field", field);
        e.put("message", msg);
        return e;
    }

    /**
     * 查询投资项目
     *
     * @param projectId 投资项目ID
     * @return 投资项目
     */
    @Override
    public InvestProject selectInvestProjectById(Long projectId) {
        return investProjectMapper.selectInvestProjectById(projectId);
    }

    /**
     * 查询投资项目列表
     *
     * @param project 投资项目
     * @return 投资项目
     */
    @Override
    public List<InvestProject> selectInvestProjectList(InvestProject project) {
        return investProjectMapper.selectInvestProjectList(project);
    }

    @Override
    public List<InvestProject> selectInvestProjectBySchemeId(Long schemeId) {
        return investProjectMapper.selectInvestProjectBySchemeId(schemeId);
    }

    /**
     * 回填规划方案冗余字段
     */
    private void fillSchemeInfo(InvestProject project) {
        if (project == null || project.getSchemeId() == null) {
            return;
        }
        PlanScheme scheme = planSchemeService.selectPlanSchemeById(project.getSchemeId());
        if (scheme != null) {
            project.setSchemeCode(scheme.getSchemeCode());
            project.setSchemeName(scheme.getSchemeName());
            project.setPlanYear(scheme.getPlanYear());
        }
    }

    /**
     * 新增投资项目
     *
     * @param project 投资项目
     * @return 结果
     */
    @Override
    public int insertInvestProject(InvestProject project) {
        fillSchemeInfo(project);
        project.setCreateBy(ShiroUtils.getLoginName());
        project.setCreateTime(DateUtils.getNowDate());
        project.setDelFlag("0");
        if (project.getProjectStatus() == null) {
            project.setProjectStatus("0"); // 草稿
        }
        return investProjectMapper.insertInvestProject(project);
    }

    /**
     * 修改投资项目
     *
     * @param project 投资项目
     * @return 结果
     */
    @Override
    public int updateInvestProject(InvestProject project) {
        fillSchemeInfo(project);
        project.setUpdateBy(ShiroUtils.getLoginName());
        project.setUpdateTime(DateUtils.getNowDate());
        return investProjectMapper.updateInvestProject(project);
    }

    /**
     * 批量删除投资项目
     *
     * @param projectIds 需要删除的投资项目ID
     * @return 结果
     */
    @Override
    public int deleteInvestProjectByIds(Long[] projectIds) {
        return investProjectMapper.deleteInvestProjectByIds(projectIds);
    }

    /**
     * 删除投资项目信息
     *
     * @param projectId 投资项目ID
     * @return 结果
     */
    @Override
    public int deleteInvestProjectById(Long projectId) {
        return investProjectMapper.deleteInvestProjectById(projectId);
    }

    @Override
    public InvestProject selectProjectByProcessInstanceId(String processInstanceId) {
        return investProjectMapper.selectProjectByProcessInstanceId(processInstanceId);
    }

    @Override
    public boolean submitProject(Long projectId, Long userId) {
        InvestProject project = selectInvestProjectById(projectId);
        if (project == null) {
            return false;
        }
        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectId", projectId);
        variables.put("schemeId", project.getSchemeId());
        variables.put("schemeName", project.getSchemeName());
        variables.put("initiatorId", userId);
        variables.put("initiatorName", ShiroUtils.getSysUser().getUserName());
        variables.put("projectName", project.getProjectName());
        variables.put("proposedAmount", project.getProposedAmount());

        // 启动流程
        ProcessInstance processInstance = bpmnProcessService.startProcess(
                "invest_project_approval",
                projectId.toString(),
                variables
        );

        // 更新项目状态和流程实例ID
        project.setProcessInstanceId(processInstance.getId());
        project.setProjectStatus("1"); // 审批中
        project.setUpdateBy(ShiroUtils.getLoginName());
        project.setUpdateTime(DateUtils.getNowDate());
        updateInvestProject(project);

        return true;
    }
}
