package com.ruoyi.modules.invest.execution.milestone.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import com.ruoyi.modules.invest.execution.milestone.service.IInvestProjectMilestoneService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/execution/milestone")
public class InvestProjectMilestoneController extends BaseController {
    @Autowired private IInvestProjectMilestoneService service;
    @RequiresPermissions("invest:execution:milestone:list") @GetMapping("/list") public TableDataInfo list(InvestProjectMilestone m){startPage();return getDataTable(service.selectList(m));}
    @RequiresPermissions("invest:execution:milestone:list") @GetMapping("/project/{projectId}/tree") public AjaxResult tree(@PathVariable Long projectId){return success(service.selectByProjectId(projectId));}
    @RequiresPermissions("invest:execution:milestone:list") @GetMapping("/{id}") public AjaxResult get(@PathVariable Long id){return success(service.selectById(id));}
    @RequiresPermissions("invest:execution:milestone:add") @Log(title="里程碑",businessType= BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestProjectMilestone m){return toAjax(service.insert(m));}
    @RequiresPermissions("invest:execution:milestone:edit") @Log(title="里程碑",businessType= BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestProjectMilestone m){return toAjax(service.update(m));}
    @RequiresPermissions("invest:execution:milestone:remove") @Log(title="里程碑",businessType= BusinessType.DELETE) @DeleteMapping("/{ids}") public AjaxResult remove(@PathVariable Long[] ids){return toAjax(service.deleteByIds(ids));}
    @RequiresPermissions("invest:execution:milestone:complete") @Log(title="里程碑完成",businessType= BusinessType.UPDATE) @PostMapping("/{id}/complete") public AjaxResult complete(@PathVariable Long id){return toAjax(service.complete(id));}
    @RequiresPermissions("invest:execution:milestone:edit") @Log(title="里程碑验收",businessType= BusinessType.UPDATE) @PostMapping("/{id}/accept") public AjaxResult accept(@PathVariable Long id, @RequestParam(required=false) String opinion){return toAjax(service.accept(id, opinion));}
    @RequiresPermissions("invest:execution:milestone:edit") @Log(title="里程碑验收驳回",businessType= BusinessType.UPDATE) @PostMapping("/{id}/reject-acceptance") public AjaxResult rejectAcceptance(@PathVariable Long id, @RequestParam(required=false) String opinion){return toAjax(service.rejectAcceptance(id, opinion));}
}
