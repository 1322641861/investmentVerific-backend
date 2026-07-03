package com.ruoyi.modules.invest.execution.report.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReport;
import com.ruoyi.modules.invest.execution.report.domain.dto.ProgressReviewRequest;
import com.ruoyi.modules.invest.execution.report.service.IInvestProgressReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/execution/report")
public class InvestProgressReportController extends BaseController {
 @Autowired private IInvestProgressReportService service;
 @RequiresPermissions("invest:execution:report:list") @GetMapping("/list") public TableDataInfo list(InvestProgressReport r){startPage();return getDataTable(service.selectList(r));}
 @RequiresPermissions("invest:execution:report:list") @GetMapping("/{id}") public AjaxResult get(@PathVariable Long id){return success(service.selectById(id));}
 @RequiresPermissions("invest:execution:report:add") @Log(title="进度上报",businessType= BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestProgressReport r){return toAjax(service.insert(r));}
 @RequiresPermissions("invest:execution:report:edit") @Log(title="进度上报",businessType= BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestProgressReport r){return toAjax(service.update(r));}
 @RequiresPermissions("invest:execution:report:remove") @DeleteMapping("/{ids}") public AjaxResult remove(@PathVariable Long[] ids){return toAjax(service.deleteByIds(ids));}
 @RequiresPermissions("invest:execution:report:submit") @PostMapping("/submit/{id}") public AjaxResult submit(@PathVariable Long id){return toAjax(service.submit(id));}
 @RequiresPermissions("invest:execution:report:submit") @PostMapping("/withdraw/{id}") public AjaxResult withdraw(@PathVariable Long id){return toAjax(service.withdraw(id));}
 @RequiresPermissions("invest:execution:review:handle") @PostMapping("/review/{id}") public AjaxResult review(@PathVariable Long id,@RequestBody ProgressReviewRequest req){return toAjax(service.review(id,req));}
}
