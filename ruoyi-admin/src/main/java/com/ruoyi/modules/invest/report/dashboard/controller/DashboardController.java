package com.ruoyi.modules.invest.report.dashboard.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.modules.invest.report.dashboard.service.IDashboardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invest/report/dashboard")
public class DashboardController extends BaseController {
    @Autowired
    private IDashboardService dashboardService;

    @RequiresPermissions("invest:report:dashboard:view")
    @GetMapping("/data")
    public AjaxResult data() { return success(dashboardService.getDashboardData()); }
}
