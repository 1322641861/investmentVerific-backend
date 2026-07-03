package com.ruoyi.modules.invest.execution.warning.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.execution.warning.domain.InvestWarningRule;
import com.ruoyi.modules.invest.execution.warning.service.IInvestWarningRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预警规则配置Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/execution/warning-rule")
public class InvestWarningRuleController extends BaseController {

    @Autowired
    private IInvestWarningRuleService warningRuleService;

    @RequiresPermissions("invest:execution:warning-rule:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestWarningRule rule) {
        startPage();
        List<InvestWarningRule> list = warningRuleService.selectList(rule);
        return getDataTable(list);
    }

    @RequiresPermissions("invest:execution:warning-rule:list")
    @GetMapping("/{ruleId}")
    public AjaxResult get(@PathVariable Long ruleId) {
        return success(warningRuleService.selectById(ruleId));
    }

    @RequiresPermissions("invest:execution:warning-rule:add")
    @Log(title = "预警规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestWarningRule rule) {
        return toAjax(warningRuleService.insert(rule));
    }

    @RequiresPermissions("invest:execution:warning-rule:edit")
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestWarningRule rule) {
        return toAjax(warningRuleService.update(rule));
    }

    @RequiresPermissions("invest:execution:warning-rule:remove")
    @Log(title = "预警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}")
    public AjaxResult remove(@PathVariable Long[] ruleIds) {
        return toAjax(warningRuleService.deleteByIds(ruleIds));
    }

    @RequiresPermissions("invest:execution:warning-rule:edit")
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PutMapping("/{ruleId}/toggle")
    public AjaxResult toggle(@PathVariable Long ruleId, @RequestParam String enabled) {
        return toAjax(warningRuleService.toggleEnabled(ruleId, enabled));
    }
}
