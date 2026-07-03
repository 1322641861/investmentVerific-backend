package com.ruoyi.modules.invest.plan.fund.source.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.fund.source.domain.InvestFundSource;
import com.ruoyi.modules.invest.plan.fund.source.service.IInvestFundSourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/plan/fund/source")
public class InvestFundSourceController extends BaseController {
    @Autowired private IInvestFundSourceService sourceService;
    @RequiresPermissions("plan:fund:source:list") @GetMapping("/list") public TableDataInfo list(InvestFundSource s){startPage(); return getDataTable(sourceService.selectInvestFundSourceList(s));}
    @RequiresPermissions("plan:fund:source:list") @GetMapping("/options") public AjaxResult options(){InvestFundSource s=new InvestFundSource(); s.setStatus("0"); return success(sourceService.selectInvestFundSourceList(s));}
    @RequiresPermissions("plan:fund:source:list") @GetMapping("/{sourceId}") public AjaxResult get(@PathVariable Long sourceId){return success(sourceService.selectInvestFundSourceById(sourceId));}
    @RequiresPermissions("plan:fund:source:add") @Log(title="资金来源",businessType= BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestFundSource s){return toAjax(sourceService.insertInvestFundSource(s));}
    @RequiresPermissions("plan:fund:source:edit") @Log(title="资金来源",businessType= BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestFundSource s){return toAjax(sourceService.updateInvestFundSource(s));}
    @RequiresPermissions("plan:fund:source:remove") @Log(title="资金来源",businessType= BusinessType.DELETE) @DeleteMapping("/{sourceIds}") public AjaxResult remove(@PathVariable Long[] sourceIds){return toAjax(sourceService.deleteInvestFundSourceByIds(sourceIds));}
}
