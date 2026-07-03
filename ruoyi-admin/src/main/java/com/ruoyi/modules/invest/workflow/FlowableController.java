package com.ruoyi.modules.invest.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工作流审批Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/workflow")
public class FlowableController extends BaseController {

    @Autowired
    private BpmnProcessService bpmnProcessService;

    /**
     * 部署所有预置流程
     */
    @Log(title = "流程部署", businessType = BusinessType.OTHER)
    @PostMapping("/deploy-all")
    public AjaxResult deployAllProcesses() {
        try {
            bpmnProcessService.deployAllProcesses();
            return success("流程部署成功");
        } catch (Exception e) {
            return error("流程部署失败: " + e.getMessage());
        }
    }

    /**
     * 获取流程定义列表
     */
    @GetMapping("/process-definitions")
    public TableDataInfo getProcessDefinitions() {
        startPage();
        List<Map<String, Object>> definitions = bpmnProcessService.getProcessDefinitions();
        return getDataTable(definitions);
    }

    /**
     * 获取部署列表
     */
    @GetMapping("/deployments")
    public TableDataInfo getDeployments() {
        startPage();
        List<Map<String, Object>> deployments = bpmnProcessService.getDeployments();
        return getDataTable(deployments);
    }

    /**
     * 删除部署
     */
    @Log(title = "流程部署", businessType = BusinessType.DELETE)
    @DeleteMapping("/deployment/{deploymentId}")
    public AjaxResult deleteDeployment(@PathVariable String deploymentId) {
        try {
            bpmnProcessService.deleteDeployment(deploymentId);
            return success("部署删除成功");
        } catch (Exception e) {
            return error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 部署单个流程
     */
    @Log(title = "流程部署", businessType = BusinessType.OTHER)
    @PostMapping("/deploy")
    public AjaxResult deployProcess(@RequestParam String bpmnFilePath, @RequestParam String processName) {
        try {
            Deployment deployment = bpmnProcessService.deployProcess(bpmnFilePath, processName);
            return success(deployment);
        } catch (Exception e) {
            return error("流程部署失败: " + e.getMessage());
        }
    }

    /**
     * 获取待办任务列表
     */
    @GetMapping("/todo")
    public TableDataInfo getTodoTasks(@RequestParam String assignee) {
        startPage();
        List<Task> tasks = bpmnProcessService.getTodoTasks(assignee);
        return getDataTable(tasks);
    }

    /**
     * 获取待办任务数量
     */
    @GetMapping("/todo/count")
    public AjaxResult getTodoCount(@RequestParam String assignee) {
        long count = bpmnProcessService.getTodoTaskCount(assignee);
        return success(count);
    }

    /**
     * 获取我发起的流程
     */
    @GetMapping("/my-started")
    public TableDataInfo getMyStartedProcesses(@RequestParam String starter) {
        startPage();
        List<Map<String, Object>> processes = bpmnProcessService.getMyStartedProcesses(starter);
        return getDataTable(processes);
    }

    /**
     * 获取我的历史任务（已办）
     */
    @GetMapping("/my-history")
    public TableDataInfo getMyHistoryTasks(@RequestParam String assignee) {
        startPage();
        List<Map<String, Object>> tasks = bpmnProcessService.getMyHistoryTasks(assignee);
        return getDataTable(tasks);
    }

    /**
     * 获取流程历史
     */
    @GetMapping("/process/{processInstanceId}/history")
    public AjaxResult getProcessHistory(@PathVariable String processInstanceId) {
        List<Map<String, Object>> history = bpmnProcessService.getProcessHistory(processInstanceId);
        return success(history);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/task/{taskId}")
    public AjaxResult getTask(@PathVariable String taskId) {
        Task task = bpmnProcessService.getTaskById(taskId);
        return success(task);
    }

    /**
     * 启动流程
     */
    @Log(title = "流程启动", businessType = BusinessType.INSERT)
    @PostMapping("/process/start")
    public AjaxResult startProcess(@RequestParam String processKey,
                                   @RequestParam String businessKey,
                                   @RequestBody(required = false) Map<String, Object> variables) {
        ProcessInstance processInstance = bpmnProcessService.startProcess(processKey, businessKey, variables);
        return success(processInstance);
    }

    /**
     * 完成任务（审批通过）
     */
    @Log(title = "流程审批", businessType = BusinessType.UPDATE)
    @PostMapping("/task/{taskId}/approve")
    public AjaxResult approveTask(@PathVariable String taskId,
                                  @RequestBody(required = false) Map<String, Object> variables) {
        bpmnProcessService.completeTask(taskId, variables, "approve");
        return success();
    }

    /**
     * 驳回任务
     */
    @Log(title = "流程审批", businessType = BusinessType.UPDATE)
    @PostMapping("/task/{taskId}/reject")
    public AjaxResult rejectTask(@PathVariable String taskId,
                                 @RequestBody(required = false) Map<String, Object> variables) {
        bpmnProcessService.completeTask(taskId, variables, "reject");
        return success();
    }

    /**
     * 签收任务
     */
    @PostMapping("/task/{taskId}/claim")
    public AjaxResult claimTask(@PathVariable String taskId, @RequestParam String userId) {
        bpmnProcessService.claimTask(taskId, userId);
        return success();
    }

    /**
     * 终止流程
     */
    @Log(title = "流程终止", businessType = BusinessType.DELETE)
    @PostMapping("/process/{processInstanceId}/terminate")
    public AjaxResult terminateProcess(@PathVariable String processInstanceId,
                                        @RequestParam String reason) {
        try {
            bpmnProcessService.terminateProcessInstance(processInstanceId, reason);
            return success("流程终止成功");
        } catch (Exception e) {
            return error("流程终止失败: " + e.getMessage());
        }
    }
}
