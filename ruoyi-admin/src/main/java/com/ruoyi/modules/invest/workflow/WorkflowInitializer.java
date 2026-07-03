package com.ruoyi.modules.invest.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 工作流初始化器
 * 在应用启动时自动部署流程定义
 *
 * @author investvf
 */
@Component
public class WorkflowInitializer {

    @Autowired
    private BpmnProcessService bpmnProcessService;

    /**
     * 应用启动完成后自动部署流程
     */
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        try {
            bpmnProcessService.deployAllProcesses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
