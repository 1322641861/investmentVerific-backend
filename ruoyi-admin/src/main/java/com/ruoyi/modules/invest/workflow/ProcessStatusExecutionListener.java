package com.ruoyi.modules.invest.workflow;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.modules.invest.message.domain.InvestMessage;
import com.ruoyi.modules.invest.message.service.IInvestMessageService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流程状态更新监听器
 * 监听流程结束事件，根据审批结果自动回写业务单据状态
 * 同时在审批通过/驳回时向发起人发送消息通知
 *
 * @author investvf
 */
@Component("processStatusExecutionListener")
public class ProcessStatusExecutionListener implements ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ProcessStatusExecutionListener.class);

    @Autowired
    private BusinessStatusCallbackService businessStatusCallbackService;

    @Autowired(required = false)
    private IInvestMessageService messageService;

    /** 流程KEY与业务名称映射 */
    private static final java.util.Map<String, String> BIZ_NAMES = new java.util.HashMap<>();
    static {
        BIZ_NAMES.put("plan_scheme_approval", "规划方案");
        BIZ_NAMES.put("budget_approval", "预算审批");
        BIZ_NAMES.put("progress-approval", "进度上报");
        BIZ_NAMES.put("benefit_calculation_approval", "效益测算");
    }

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        String processDefinitionId = execution.getProcessDefinitionId();
        String businessKey = execution.getProcessInstanceBusinessKey();
        String processDefinitionKey = extractProcessKey(processDefinitionId);

        log.info("流程执行监听触发: event={}, processKey={}, businessKey={}",
                eventName, processDefinitionKey, businessKey);

        if (!"end".equals(eventName)) return;
        if (businessKey == null || businessKey.isEmpty()) return;

        Object outcomeObj = execution.getVariable("outcome");
        String outcome = outcomeObj != null ? outcomeObj.toString() : "";
        boolean approved = "approve".equals(outcome);
        if (!approved) {
            String activityId = execution.getCurrentActivityId();
            if (activityId != null) approved = activityId.contains("Approved");
        }

        log.info("审批结果判定: outcome={}, 最终={}", outcome, approved ? "通过" : "驳回");

        // 回写业务状态
        try {
            businessStatusCallbackService.updateBusinessStatus(processDefinitionKey, businessKey, approved);
        } catch (Exception e) {
            log.error("业务状态回写失败", e);
        }

        // 发送审批结果消息通知
        sendApprovalMessage(execution, processDefinitionKey, businessKey, approved);
    }

    private void sendApprovalMessage(DelegateExecution execution, String processKey, String businessKey, boolean approved) {
        if (messageService == null) return;
        try {
            Object initiatorId = execution.getVariable("initiatorId");
            Object initiatorName = execution.getVariable("initiatorName");
            Long receiverId = initiatorId != null ? Long.valueOf(initiatorId.toString()) : null;
            if (receiverId == null) return;

            String bizName = BIZ_NAMES.getOrDefault(processKey, "业务");
            InvestMessage msg = new InvestMessage();
            msg.setMessageType("2"); // 审批通知
            msg.setTitle(bizName + "审批" + (approved ? "通过" : "驳回"));
            msg.setContent("您的" + bizName + "申请已" + (approved ? "审批通过" : "被驳回") + "，业务编号：" + businessKey);
            msg.setReceiverId(receiverId);
            msg.setReceiverName(initiatorName != null ? initiatorName.toString() : "");
            msg.setBusinessType(processKey);
            msg.setBusinessId(Long.valueOf(businessKey));
            msg.setCreateBy("system");
            msg.setCreateTime(DateUtils.getNowDate());
            messageService.sendMessage(msg);
            log.info("已向用户[{}]发送审批结果消息", receiverId);
        } catch (Exception e) {
            log.warn("发送审批结果消息失败", e);
        }
    }

    private String extractProcessKey(String processDefinitionId) {
        if (processDefinitionId == null) return "";
        String[] parts = processDefinitionId.split(":");
        return parts.length > 0 ? parts[0] : "";
    }
}
