package com.ruoyi.modules.system.config.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.system.config.domain.SysBusinessConfig;
import com.ruoyi.modules.system.config.service.ISysBusinessConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/business-config")
public class SysBusinessConfigController extends BaseController {
    @Autowired private ISysBusinessConfigService configService;

    @RequiresPermissions("system:businessConfig:list")
    @GetMapping("/list") public TableDataInfo list(SysBusinessConfig config) { startPage(); return getDataTable(configService.selectList(config)); }

    @RequiresPermissions("system:businessConfig:list")
    @GetMapping("/{configId}") public AjaxResult get(@PathVariable Long configId) { return success(configService.selectById(configId)); }

    @RequiresPermissions("system:businessConfig:list")
    @GetMapping("/code/{configCode}") public AjaxResult getByCode(@PathVariable String configCode) { return success(configService.selectByCode(configCode)); }

    @RequiresPermissions("system:businessConfig:add")
    @Log(title="业务参数", businessType=BusinessType.INSERT)
    @PostMapping public AjaxResult add(@RequestBody SysBusinessConfig config) { return toAjax(configService.insert(config)); }

    @RequiresPermissions("system:businessConfig:edit")
    @Log(title="业务参数", businessType=BusinessType.UPDATE)
    @PutMapping public AjaxResult edit(@RequestBody SysBusinessConfig config) { return toAjax(configService.update(config)); }

    @RequiresPermissions("system:businessConfig:remove")
    @Log(title="业务参数", businessType=BusinessType.DELETE)
    @DeleteMapping("/{configIds}") public AjaxResult remove(@PathVariable Long[] configIds) { return toAjax(configService.deleteByIds(configIds)); }
}
