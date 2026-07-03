package com.ruoyi.modules.invest.execution.warning.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.execution.warning.domain.InvestProgressWarning;
import com.ruoyi.modules.invest.execution.warning.service.IInvestProgressWarningService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/execution/warning")
public class InvestProgressWarningController extends BaseController {
    @Autowired private IInvestProgressWarningService service;

    @RequiresPermissions("invest:execution:warning:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestProgressWarning w) { startPage(); return getDataTable(service.selectList(w)); }

    @RequiresPermissions("invest:execution:warning:scan")
    @PostMapping("/scan")
    public AjaxResult scan(@RequestParam(required = false) Long projectId) {
        int count = (projectId != null) ? service.scan(projectId) : service.scanAllProjects();
        return success("扫描完成，共生成" + count + "条预警");
    }

    @RequiresPermissions("invest:execution:warning:handle")
    @PostMapping("/resolve/{id}")
    public AjaxResult resolve(@PathVariable Long id, @RequestParam(required = false) String opinion) {
        return toAjax(service.handle(id, "2", opinion));
    }

    @RequiresPermissions("invest:execution:warning:handle")
    @PostMapping("/ignore/{id}")
    public AjaxResult ignore(@PathVariable Long id, @RequestParam(required = false) String opinion) {
        return toAjax(service.handle(id, "3", opinion));
    }
}
