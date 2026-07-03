package com.ruoyi.modules.invest.report.widget.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.report.widget.domain.InvestDashboardWidget;
import com.ruoyi.modules.invest.report.widget.service.IInvestDashboardWidgetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/report/widget")
public class InvestDashboardWidgetController extends BaseController {
    @Autowired
    private IInvestDashboardWidgetService service;

    @RequiresPermissions("invest:report:widget:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestDashboardWidget widget) { startPage(); return getDataTable(service.selectList(widget)); }

    @RequiresPermissions("invest:report:widget:list")
    @GetMapping("/enabled")
    public AjaxResult enabled() { return success(service.selectAllEnabled()); }

    @RequiresPermissions("invest:report:widget:list")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable Long id) { return success(service.selectById(id)); }

    @RequiresPermissions("invest:report:widget:add")
    @Log(title="看板组件",businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestDashboardWidget widget) { return toAjax(service.insert(widget)); }

    @RequiresPermissions("invest:report:widget:edit")
    @Log(title="看板组件",businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestDashboardWidget widget) { return toAjax(service.update(widget)); }

    @RequiresPermissions("invest:report:widget:remove")
    @Log(title="看板组件",businessType=BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(service.deleteByIds(ids)); }
}
