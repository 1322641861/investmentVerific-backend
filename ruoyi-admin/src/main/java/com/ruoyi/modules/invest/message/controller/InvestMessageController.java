package com.ruoyi.modules.invest.message.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.message.domain.InvestMessage;
import com.ruoyi.modules.invest.message.service.IInvestMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invest/message")
public class InvestMessageController extends BaseController {

    @Autowired
    private IInvestMessageService messageService;

    @RequiresPermissions("invest:message:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestMessage msg) {
        startPage();
        List<InvestMessage> list = messageService.selectList(msg);
        return getDataTable(list);
    }

    @RequiresPermissions("invest:message:list")
    @GetMapping("/unread-count")
    public AjaxResult unreadCount(@RequestParam Long receiverId) {
        Long count = messageService.selectUnreadCount(receiverId);
        return success(count);
    }

    @RequiresPermissions("invest:message:edit")
    @PutMapping("/mark-read/{messageId}")
    public AjaxResult markRead(@PathVariable Long messageId) {
        return toAjax(messageService.markRead(messageId));
    }

    @RequiresPermissions("invest:message:edit")
    @PutMapping("/mark-all-read")
    public AjaxResult markAllRead(@RequestParam Long receiverId) {
        return toAjax(messageService.markAllRead(receiverId));
    }

    @RequiresPermissions("invest:message:add")
    @PostMapping("/send")
    public AjaxResult send(@RequestBody InvestMessage msg) {
        return toAjax(messageService.sendMessage(msg));
    }

    @RequiresPermissions("invest:message:remove")
    @DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds) {
        return toAjax(messageService.deleteByIds(messageIds));
    }
}
