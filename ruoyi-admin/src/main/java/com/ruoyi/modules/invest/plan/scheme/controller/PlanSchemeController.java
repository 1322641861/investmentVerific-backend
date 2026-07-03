package com.ruoyi.modules.invest.plan.scheme.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTag;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeService;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeTagService;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeVersionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规划方案Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/plan/scheme")
public class PlanSchemeController extends BaseController {

    @Autowired
    private IPlanSchemeService planSchemeService;

    @Autowired
    private IPlanSchemeVersionService versionService;

    @Autowired
    private IPlanSchemeTagService tagService;

    /** 查询规划方案列表 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/list")
    public TableDataInfo list(PlanScheme planScheme) {
        startPage();
        List<PlanScheme> list = planSchemeService.selectPlanSchemeList(planScheme);
        return getDataTable(list);
    }

    /** 获取规划方案详细信息 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/{schemeId}")
    public AjaxResult getInfo(@PathVariable Long schemeId) {
        return success(planSchemeService.selectPlanSchemeById(schemeId));
    }

    /** 新增规划方案 */
    @RequiresPermissions("plan:scheme:add")
    @Log(title = "规划方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlanScheme planScheme) {
        return toAjax(planSchemeService.insertPlanScheme(planScheme));
    }

    /** 修改规划方案 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlanScheme planScheme) {
        return toAjax(planSchemeService.updatePlanScheme(planScheme));
    }

    /** 删除规划方案 */
    @RequiresPermissions("plan:scheme:remove")
    @Log(title = "规划方案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schemeIds}")
    public AjaxResult remove(@PathVariable Long[] schemeIds) {
        return toAjax(planSchemeService.deletePlanSchemeByIds(schemeIds));
    }

    /** 提交审批 */
    @RequiresPermissions("plan:scheme:submit")
    @Log(title = "规划方案提交审批", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{schemeId}")
    public AjaxResult submit(@PathVariable Long schemeId) {
        return toAjax(planSchemeService.submitApproval(schemeId));
    }

    /** 查询版本列表 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/version/list/{schemeId}")
    public TableDataInfo versionList(@PathVariable Long schemeId) {
        startPage();
        List<PlanSchemeVersion> list = versionService.selectVersionListBySchemeId(schemeId);
        return getDataTable(list);
    }

    /** 创建版本快照 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案版本", businessType = BusinessType.INSERT)
    @PostMapping("/version")
    public AjaxResult createVersion(@RequestBody PlanSchemeVersion version) {
        if (version.getSchemeId() == null) {
            return AjaxResult.error("方案ID不能为空");
        }
        PlanSchemeVersion result = planSchemeService.createVersion(version.getSchemeId(), version.getChangeLog());
        return result == null ? AjaxResult.error("方案不存在") : success(result);
    }

    /** 版本回滚 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案版本", businessType = BusinessType.UPDATE)
    @PostMapping("/version/rollback/{versionId}")
    public AjaxResult rollbackVersion(@PathVariable Long versionId) {
        return toAjax(planSchemeService.rollbackVersion(versionId));
    }

    /** 版本对比 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/version/compare/{v1}/{v2}")
    public AjaxResult compareVersions(@PathVariable Long v1, @PathVariable Long v2) {
        return success(planSchemeService.compareVersions(v1, v2));
    }

    /** 查询标签列表 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/tag/list")
    public TableDataInfo tagList(PlanSchemeTag tag) {
        startPage();
        List<PlanSchemeTag> list = tagService.selectPlanSchemeTagList(tag);
        return getDataTable(list);
    }

    /** 查询指定方案标签 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/{schemeId}/tags")
    public AjaxResult schemeTags(@PathVariable Long schemeId) {
        return success(tagService.selectTagsBySchemeId(schemeId));
    }

    /** 新增标签 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案标签", businessType = BusinessType.INSERT)
    @PostMapping("/tag")
    public AjaxResult addTag(@RequestBody PlanSchemeTag tag) {
        return toAjax(tagService.insertPlanSchemeTag(tag));
    }

    /** 修改标签 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案标签", businessType = BusinessType.UPDATE)
    @PutMapping("/tag")
    public AjaxResult editTag(@RequestBody PlanSchemeTag tag) {
        return toAjax(tagService.updatePlanSchemeTag(tag));
    }

    /** 删除标签 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/tag/{tagIds}")
    public AjaxResult removeTag(@PathVariable Long[] tagIds) {
        return toAjax(tagService.deletePlanSchemeTagByIds(tagIds));
    }

    /** 绑定标签 */
    @RequiresPermissions("plan:scheme:edit")
    @Log(title = "规划方案标签绑定", businessType = BusinessType.UPDATE)
    @PutMapping("/{schemeId}/tags")
    public AjaxResult bindTags(@PathVariable Long schemeId, @RequestBody Long[] tagIds) {
        return toAjax(planSchemeService.bindTags(schemeId, tagIds));
    }

    /** 获取标签选择数据 */
    @RequiresPermissions("plan:scheme:list")
    @GetMapping("/tag/options")
    public AjaxResult tagOptions() {
        PlanSchemeTag query = new PlanSchemeTag();
        query.setStatus("0");
        List<PlanSchemeTag> list = tagService.selectPlanSchemeTagList(query);
        Map<String, Object> result = new HashMap<>();
        result.put("tags", list);
        return success(result);
    }
}
