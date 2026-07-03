package com.ruoyi.modules.invest.duediligence.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.duediligence.domain.DueDiligence;
import com.ruoyi.modules.invest.duediligence.service.IDueDiligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 尽职调查Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/duediligence")
public class DueDiligenceController extends BaseController {

    @Autowired
    private IDueDiligenceService dueDiligenceService;

    @GetMapping("/list")
    public TableDataInfo list(DueDiligence dueDiligence) {
        startPage();
        List<DueDiligence> list = dueDiligenceService.selectDueDiligenceList(dueDiligence);
        return getDataTable(list);
    }

    @GetMapping("/{dueDiligenceId}")
    public AjaxResult getInfo(@PathVariable Long dueDiligenceId) {
        DueDiligence dueDiligence = dueDiligenceService.selectDueDiligenceById(dueDiligenceId);
        return success(dueDiligence);
    }

    @GetMapping("/byProject/{projectId}")
    public AjaxResult getByProject(@PathVariable Long projectId) {
        DueDiligence dueDiligence = dueDiligenceService.selectByProjectId(projectId);
        return success(dueDiligence);
    }

    @Log(title = "尽职调查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DueDiligence dueDiligence) {
        return toAjax(dueDiligenceService.insertDueDiligence(dueDiligence));
    }

    @Log(title = "尽职调查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DueDiligence dueDiligence) {
        return toAjax(dueDiligenceService.updateDueDiligence(dueDiligence));
    }

    @Log(title = "尽职调查", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dueDiligenceService.deleteDueDiligenceByIds(ids));
    }
}
