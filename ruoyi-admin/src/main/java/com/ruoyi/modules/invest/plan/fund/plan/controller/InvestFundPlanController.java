package com.ruoyi.modules.invest.plan.fund.plan.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlan;
import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import com.ruoyi.modules.invest.plan.fund.plan.service.IInvestFundPlanService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/plan/fund/plan")
public class InvestFundPlanController extends BaseController {
    @Autowired private IInvestFundPlanService planService;
    @RequiresPermissions("plan:fund:plan:list") @GetMapping("/list") public TableDataInfo list(InvestFundPlan p){startPage(); return getDataTable(planService.selectInvestFundPlanList(p));}
    @RequiresPermissions("plan:fund:plan:list") @GetMapping("/{planId}") public AjaxResult get(@PathVariable Long planId){return success(planService.selectInvestFundPlanById(planId));}
    @RequiresPermissions("plan:fund:plan:list") @GetMapping("/project/{projectId}") public AjaxResult getByProject(@PathVariable Long projectId){return success(planService.selectInvestFundPlanByProjectId(projectId));}
    @RequiresPermissions("plan:fund:plan:add") @Log(title="资金配套计划",businessType=BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestFundPlan p){return toAjax(planService.insertInvestFundPlan(p));}
    @RequiresPermissions("plan:fund:plan:edit") @Log(title="资金配套计划",businessType=BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestFundPlan p){return toAjax(planService.updateInvestFundPlan(p));}
    @RequiresPermissions("plan:fund:plan:remove") @Log(title="资金配套计划",businessType=BusinessType.DELETE) @DeleteMapping("/{planIds}") public AjaxResult remove(@PathVariable Long[] planIds){return toAjax(planService.deleteInvestFundPlanByIds(planIds));}
    @RequiresPermissions("plan:fund:plan:list") @GetMapping("/{planId}/items") public AjaxResult items(@PathVariable Long planId){return success(planService.selectItemsByPlanId(planId));}
    @RequiresPermissions("plan:fund:plan:allocate") @Log(title="资金配套明细",businessType=BusinessType.UPDATE) @PostMapping("/{planId}/items") public AjaxResult saveItems(@PathVariable Long planId,@RequestBody List<InvestFundPlanItem> items){return toAjax(planService.savePlanItems(planId, items));}
    @RequiresPermissions("plan:fund:plan:validate") @Log(title="资金配套校验",businessType=BusinessType.OTHER) @PostMapping("/{planId}/validate") public AjaxResult validate(@PathVariable Long planId){return success(planService.validatePlan(planId,"1"));}
    @RequiresPermissions("plan:fund:plan:submit") @Log(title="资金配套提交",businessType=BusinessType.UPDATE) @PostMapping("/submit/{planId}") public AjaxResult submit(@PathVariable Long planId){return toAjax(planService.submitPlan(planId));}
}