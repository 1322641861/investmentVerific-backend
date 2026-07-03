package com.ruoyi.modules.invest.plan.fund.roi.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.plan.fund.roi.service.IFundRoiService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan/fund/roi")
public class FundRoiController extends BaseController {

    @Autowired
    private IFundRoiService fundRoiService;

    @RequiresPermissions("plan:fund:roi:list")
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        return success(fundRoiService.getDashboard());
    }

    @RequiresPermissions("plan:fund:roi:list")
    @GetMapping("/project-list")
    public TableDataInfo projectList(@RequestParam(required = false) Integer planYear,
                                     @RequestParam(required = false, defaultValue = "roi") String orderBy) {
        startPage();
        List<Map<String, Object>> list = fundRoiService.selectProjectRoiList(planYear, orderBy);
        return getDataTable(list);
    }

    @RequiresPermissions("plan:fund:roi:list")
    @GetMapping("/monthly-trend")
    public AjaxResult monthlyTrend(@RequestParam(required = false) Integer year) {
        if (year == null) year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return success(fundRoiService.selectMonthlyTrend(year));
    }
}
