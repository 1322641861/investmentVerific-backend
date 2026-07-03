package com.ruoyi.modules.invest.project.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.service.IInvestProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投资项目Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/project")
public class InvestProjectController extends BaseController {

    @Autowired
    private IInvestProjectService investProjectService;

    /**
     * 查询投资项目列表
     */
    @RequiresPermissions("invest:project:list")
    @GetMapping("/list")
    public TableDataInfo list(InvestProject project) {
        startPage();
        List<InvestProject> list = investProjectService.selectInvestProjectList(project);
        return getDataTable(list);
    }

    /**
     * 获取投资项目详细信息
     */
    @RequiresPermissions("invest:project:list")
    @GetMapping("/{projectId}")
    public AjaxResult getInfo(@PathVariable Long projectId) {
        InvestProject project = investProjectService.selectInvestProjectById(projectId);
        return success(project);
    }

    /**
     * 新增投资项目
     */
    @RequiresPermissions("invest:project:add")
    @Log(title = "投资项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InvestProject project) {
        return toAjax(investProjectService.insertInvestProject(project));
    }

    /**
     * 修改投资项目
     */
    @RequiresPermissions("invest:project:edit")
    @Log(title = "投资项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InvestProject project) {
        return toAjax(investProjectService.updateInvestProject(project));
    }

    /**
     * 删除投资项目
     */
    @RequiresPermissions("invest:project:remove")
    @Log(title = "投资项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds) {
        return toAjax(investProjectService.deleteInvestProjectByIds(projectIds));
    }

    @RequiresPermissions("invest:project:submit")
    @Log(title = "投资项目审批", businessType = BusinessType.GRANT)
    @PostMapping("/submit/{projectId}")
    public AjaxResult submit(@PathVariable Long projectId) {
        Long userId = ShiroUtils.getUserId();
        boolean result = investProjectService.submitProject(projectId, userId);
        return result ? success("审批流程已启动") : AjaxResult.error("启动审批流程失败");
    }

    @RequiresPermissions("invest:project:edit")
    @Log(title = "项目状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status/{projectId}/{targetStatus}")
    public AjaxResult transition(@PathVariable Long projectId, @PathVariable String targetStatus) {
        return success(investProjectService.transitionStatus(projectId, targetStatus));
    }

    @RequiresPermissions("invest:project:list")
    @GetMapping("/compliance/{projectId}")
    public AjaxResult compliance(@PathVariable Long projectId) {
        return success(investProjectService.validateCompliance(projectId));
    }
}
