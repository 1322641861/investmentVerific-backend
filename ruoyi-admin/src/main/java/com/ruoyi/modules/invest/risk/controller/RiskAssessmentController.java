package com.ruoyi.modules.invest.risk.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.risk.domain.RiskAssessment;
import com.ruoyi.modules.invest.risk.service.IRiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 风险评估Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/invest/risk")
public class RiskAssessmentController extends BaseController {

    @Autowired
    private IRiskAssessmentService riskAssessmentService;

    @GetMapping("/list")
    public TableDataInfo list(RiskAssessment riskAssessment) {
        startPage();
        List<RiskAssessment> list = riskAssessmentService.selectRiskAssessmentList(riskAssessment);
        return getDataTable(list);
    }

    @GetMapping("/{riskId}")
    public AjaxResult getInfo(@PathVariable Long riskId) {
        RiskAssessment riskAssessment = riskAssessmentService.selectRiskAssessmentById(riskId);
        return success(riskAssessment);
    }

    @GetMapping("/byProject/{projectId}")
    public AjaxResult getByProject(@PathVariable Long projectId) {
        RiskAssessment riskAssessment = riskAssessmentService.selectByProjectId(projectId);
        return success(riskAssessment);
    }

    @Log(title = "风险评估", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskAssessment riskAssessment) {
        return toAjax(riskAssessmentService.insertRiskAssessment(riskAssessment));
    }

    @Log(title = "风险评估", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiskAssessment riskAssessment) {
        return toAjax(riskAssessmentService.updateRiskAssessment(riskAssessment));
    }

    @Log(title = "风险评估", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(riskAssessmentService.deleteRiskAssessmentByIds(ids));
    }
}
