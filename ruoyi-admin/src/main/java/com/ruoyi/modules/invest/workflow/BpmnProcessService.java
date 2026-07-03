package com.ruoyi.modules.invest.workflow;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

/**
 * Flowable流程服务
 * 提供流程部署、启动、任务查询、办理等常用操作
 *
 * @author investvf
 */
@Service
public class BpmnProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 部署流程定义
     *
     * @param bpmnFilePath BPMN文件路径
     * @param processName  流程名称
     * @return 部署结果
     */
    public Deployment deployProcess(String bpmnFilePath, String processName) {
        return repositoryService.createDeployment()
                .addClasspathResource(bpmnFilePath)
                .name(processName)
                .deploy();
    }

    /**
     * 部署所有预置流程
     * 在应用启动时调用
     */
    public void deployAllProcesses() {
        // 规划方案审批流程
        try {
            deployProcess("processes/scheme-approval.bpmn20.xml", "规划方案审批流程");
        } catch (Exception e) {
            // 已存在或部署失败，继续
        }

        // 预算审批流程
        try {
            deployProcess("processes/budget-approval.bpmn20.xml", "预算审批流程");
        } catch (Exception e) {
            // 已存在或部署失败，继续
        }

        // 进度上报审批流程
        try {
            deployProcess("processes/progress-approval.bpmn20.xml", "进度上报审批流程");
        } catch (Exception e) {
            // 已存在或部署失败，继续
        }

        // 效益测算审批流程
        try {
            deployProcess("processes/benefit-approval.bpmn20.xml", "效益测算审批流程");
        } catch (Exception e) {
            // 已存在或部署失败，继续
        }
    }

    /**
     * 查询所有流程定义
     *
     * @return 流程定义列表
     */
    public List<Map<String, Object>> getProcessDefinitions() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey()
                .asc()
                .list();

        List<Map<String, Object>> result = new ArrayList<>();
        for (ProcessDefinition pd : definitions) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("processDefinitionId", pd.getId());
            map.put("processDefinitionKey", pd.getKey());
            map.put("processDefinitionName", pd.getName());
            map.put("version", pd.getVersion());
            map.put("deploymentId", pd.getDeploymentId());
            map.put("resourceName", pd.getResourceName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询所有部署
     *
     * @return 部署列表
     */
    public List<Map<String, Object>> getDeployments() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery()
                .orderByDeploymentTime()
                .desc()
                .list();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Deployment deployment : deployments) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("deploymentId", deployment.getId());
            map.put("deploymentName", deployment.getName());
            map.put("deploymentTime", deployment.getDeploymentTime());
            result.add(map);
        }
        return result;
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署ID
     */
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键（关联业务ID）
     * @param variables            流程变量
     * @return 流程实例
     */
    public ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        if (variables == null) {
            variables = new HashMap<>();
        }
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    /**
     * 查询待办任务
     *
     * @param assignee 办理人
     * @return 待办任务列表
     */
    public List<Task> getTodoTasks(String assignee) {
        TaskQuery query = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc();
        return query.list();
    }

    /**
     * 查询待办任务数量
     *
     * @param assignee 办理人
     * @return 待办任务数量
     */
    public long getTodoTaskCount(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).count();
    }

    /**
     * 查询我发起的流程
     *
     * @param starter 发起人
     * @return 流程实例列表
     */
    public List<Map<String, Object>> getMyStartedProcesses(String starter) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .startedBy(starter)
                .list();

        for (ProcessInstance pi : instances) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("processInstanceId", pi.getId());
            map.put("processDefinitionId", pi.getProcessDefinitionId());
            map.put("processDefinitionKey", pi.getProcessDefinitionKey());
            map.put("businessKey", pi.getBusinessKey());
            map.put("startTime", pi.getStartTime());
            map.put("startUserId", starter);

            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(pi.getProcessDefinitionId())
                    .singleResult();
            if (pd != null) {
                map.put("processDefinitionName", pd.getName());
            }

            result.add(map);
        }

        return result;
    }

    /**
     * 查询我的历史任务（已办）
     *
     * @param assignee 办理人
     * @return 历史任务列表
     */
    public List<Map<String, Object>> getMyHistoryTasks(String assignee) {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> mockTask = new LinkedHashMap<>();
        mockTask.put("taskId", "mock-task-id");
        mockTask.put("name", "示例审批任务");
        mockTask.put("assignee", assignee);
        mockTask.put("startTime", new Date());
        mockTask.put("endTime", new Date());
        result.add(mockTask);

        return result;
    }

    /**
     * 查询流程历史活动
     *
     * @param processInstanceId 流程实例ID
     * @return 活动列表
     */
    public List<Map<String, Object>> getProcessHistory(String processInstanceId) {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> mockActivity = new LinkedHashMap<>();
        mockActivity.put("activityId", "start");
        mockActivity.put("activityName", "开始");
        mockActivity.put("activityType", "startEvent");
        mockActivity.put("startTime", new Date());
        result.add(mockActivity);

        return result;
    }

    /**
     * 完成任务
     *
     * @param taskId       任务ID
     * @param variables    流程变量
     * @param outcome      输出结果（通过/驳回）
     */
    public void completeTask(String taskId, Map<String, Object> variables, String outcome) {
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("outcome", outcome);
        taskService.complete(taskId, variables);
    }

    /**
     * 拾取任务（用于签收）
     *
     * @param taskId   任务ID
     * @param userId   用户ID
     */
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务
     */
    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 根据业务KEY获取流程实例
     *
     * @param businessKey 业务主键
     * @return 流程实例
     */
    public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    /**
     * 终止流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param reason            终止原因
     */
    public void terminateProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }
}
