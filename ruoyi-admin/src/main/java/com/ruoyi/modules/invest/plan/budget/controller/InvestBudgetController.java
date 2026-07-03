package com.ruoyi.modules.invest.plan.budget.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudget;
import com.ruoyi.modules.invest.plan.budget.domain.InvestBudgetItem;
import com.ruoyi.modules.invest.plan.budget.service.IInvestBudgetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投资预算Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/plan/budget")
public class InvestBudgetController extends BaseController {

    @Autowired
    private IInvestBudgetService budgetService;

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestBudget budget) {
        startPage();
        List<InvestBudget> list = budgetService.selectInvestBudgetList(budget);
        return getDataTable(list);
    }

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/{budgetId}")
    public AjaxResult getInfo(@PathVariable Long budgetId) {
        return success(budgetService.selectInvestBudgetById(budgetId));
    }

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/scheme/{schemeId}")
    public AjaxResult getByScheme(@PathVariable Long schemeId) {
        return success(budgetService.selectInvestBudgetBySchemeId(schemeId));
    }

    @RequiresPermissions("plan:budget:add")
    @Log(title = "投资预算", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestBudget budget) {
        return toAjax(budgetService.insertInvestBudget(budget));
    }

    @RequiresPermissions("plan:budget:edit")
    @Log(title = "投资预算", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestBudget budget) {
        return toAjax(budgetService.updateInvestBudget(budget));
    }

    @RequiresPermissions("plan:budget:remove")
    @Log(title = "投资预算", businessType = BusinessType.DELETE)
    @DeleteMapping("/{budgetIds}")
    public AjaxResult remove(@PathVariable Long[] budgetIds) {
        return toAjax(budgetService.deleteInvestBudgetByIds(budgetIds));
    }

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/{budgetId}/items")
    public AjaxResult items(@PathVariable Long budgetId) {
        return success(budgetService.selectItemsByBudgetId(budgetId));
    }

    @RequiresPermissions("plan:budget:allocate")
    @Log(title = "投资预算明细", businessType = BusinessType.UPDATE)
    @PostMapping("/{budgetId}/items")
    public AjaxResult saveItems(@PathVariable Long budgetId, @RequestBody List<InvestBudgetItem> items) {
        return toAjax(budgetService.saveBudgetItems(budgetId, items));
    }

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/{budgetId}/candidate-projects")
    public AjaxResult candidateProjects(@PathVariable Long budgetId) {
        return success(budgetService.selectCandidateProjects(budgetId));
    }

    @RequiresPermissions("plan:budget:validate")
    @Log(title = "投资预算校验", businessType = BusinessType.OTHER)
    @PostMapping("/{budgetId}/validate")
    public AjaxResult validate(@PathVariable Long budgetId) {
        return success(budgetService.validateBudget(budgetId, "1"));
    }

    @RequiresPermissions("plan:budget:list")
    @GetMapping("/{budgetId}/check-logs")
    public AjaxResult checkLogs(@PathVariable Long budgetId) {
        return success(budgetService.selectCheckLogsByBudgetId(budgetId));
    }

    @RequiresPermissions("plan:budget:submit")
    @Log(title = "投资预算提交", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{budgetId}")
    public AjaxResult submit(@PathVariable Long budgetId) {
        return toAjax(budgetService.submitBudget(budgetId));
    }
}
