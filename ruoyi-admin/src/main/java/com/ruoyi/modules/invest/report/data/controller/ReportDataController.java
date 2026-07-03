package com.ruoyi.modules.invest.report.data.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.modules.invest.report.data.service.IReportDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/report/data")
public class ReportDataController extends BaseController {

    @Autowired
    private IReportDataService reportDataService;

    @RequiresPermissions("invest:report:scheme:view")
    @GetMapping("/scheme")
    public AjaxResult scheme(@RequestParam(required = false) Integer planYear) {
        return success(reportDataService.getSchemeReport(planYear));
    }

    @RequiresPermissions("invest:report:project:view")
    @GetMapping("/project")
    public AjaxResult project(@RequestParam(required = false) String projectStatus) {
        return success(reportDataService.getProjectReport(projectStatus));
    }

    @RequiresPermissions("invest:report:fund:view")
    @GetMapping("/fund")
    public AjaxResult fund(@RequestParam(required = false) Integer planYear) {
        return success(reportDataService.getFundReport(planYear));
    }

    @RequiresPermissions("invest:report:benefit:view")
    @GetMapping("/benefit")
    public AjaxResult benefit(@RequestParam(required = false) Integer planYear) {
        return success(reportDataService.getBenefitReport(planYear));
    }

    @RequiresPermissions("invest:report:progress:view")
    @GetMapping("/progress")
    public AjaxResult progress(@RequestParam(required = false) Integer planYear) {
        return success(reportDataService.getProgressReport(planYear));
    }
}
