package com.ruoyi.modules.invest.report.config.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.report.config.domain.InvestReportConfig;
import com.ruoyi.modules.invest.report.config.service.IInvestReportConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/report/config")
public class InvestReportConfigController extends BaseController {
    @Autowired
    private IInvestReportConfigService service;

    @RequiresPermissions("invest:report:config:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestReportConfig config) { startPage(); return getDataTable(service.selectList(config)); }

    @RequiresPermissions("invest:report:config:list")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable Long id) { return success(service.selectById(id)); }

    @RequiresPermissions("invest:report:config:add")
    @Log(title="报表配置",businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestReportConfig config) { return toAjax(service.insert(config)); }

    @RequiresPermissions("invest:report:config:edit")
    @Log(title="报表配置",businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestReportConfig config) { return toAjax(service.update(config)); }

    @RequiresPermissions("invest:report:config:remove")
    @Log(title="报表配置",businessType=BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteByIds(ids)); }
}
