package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /**
     * 扫描所有项目进度预警
     * 可通过系统监控→定时任务 配置定时执行
     * 调用示例: ryTask.scanProgressWarnings
     */
    public void scanProgressWarnings()
    {
        try {
            Object service = SpringUtils.getBean("investProgressWarningServiceImpl");
            if (service == null) {
                log.warn("预警服务未找到，跳过扫描");
                return;
            }
            java.lang.reflect.Method method = service.getClass().getMethod("scanAllProjects");
            log.info("开始全项目预警扫描...");
            Object result = method.invoke(service);
            log.info("全项目预警扫描完成，共生成{}条预警", result);
        } catch (Exception e) {
            log.error("预警扫描失败", e);
        }
    }

    /**
     * 生成报表数据快照并推送消息通知
     * 调用示例: ryTask.generateReport
     */
    public void generateReport()
    {
        try {
            Object dashboardService = SpringUtils.getBean("dashboardServiceImpl");
            if (dashboardService == null) {
                log.warn("报表服务未找到，跳过");
                return;
            }
            java.lang.reflect.Method method = dashboardService.getClass().getMethod("getDashboardData");
            Object data = method.invoke(dashboardService);
            log.info("报表数据生成完成: {}", data);

            // 发送消息通知给admin用户
            try {
                Object msgService = SpringUtils.getBean("investMessageServiceImpl");
                if (msgService != null) {
                    Object msg = Class.forName("com.ruoyi.modules.invest.message.domain.InvestMessage").newInstance();
                    java.lang.reflect.Method setTitle = msg.getClass().getMethod("setTitle", String.class);
                    java.lang.reflect.Method setContent = msg.getClass().getMethod("setContent", String.class);
                    java.lang.reflect.Method setMsgType = msg.getClass().getMethod("setMessageType", String.class);
                    java.lang.reflect.Method setReceiverId = msg.getClass().getMethod("setReceiverId", Long.class);
                    java.lang.reflect.Method setCreateBy = msg.getClass().getMethod("setCreateBy", String.class);
                    java.lang.reflect.Method sendMsg = msgService.getClass().getMethod("sendMessage", msg.getClass());

                    setTitle.invoke(msg, "定期报表");
                    setContent.invoke(msg, "系统已生成最新报表数据，请查看报表页面获取详情。");
                    setMsgType.invoke(msg, "4");
                    setReceiverId.invoke(msg, 1L);
                    setCreateBy.invoke(msg, "system");
                    sendMsg.invoke(msgService, msg);
                    log.info("报表消息通知已发送");
                }
            } catch (Exception e2) {
                log.warn("发送报表消息失败: {}", e2.getMessage());
            }
        } catch (Exception e) {
            log.error("报表生成失败", e);
        }
    }
}
