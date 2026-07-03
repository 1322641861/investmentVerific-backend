package com.ruoyi.modules.invest.plan.fund.tracking.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.plan.fund.tracking.service.IFundTrackingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan/fund/tracking")
public class FundTrackingController extends BaseController {

    @Autowired
    private IFundTrackingService fundTrackingService;

    @RequiresPermissions("plan:fund:tracking:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String projectName,
                              @RequestParam(required = false) String payeeName,
                              @RequestParam(required = false) String sourceName,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate) {
        startPage();
        List<Map<String, Object>> list = fundTrackingService.selectPaymentLedger(
                projectName, payeeName, sourceName, startDate, endDate);
        return getDataTable(list);
    }

    @RequiresPermissions("plan:fund:tracking:list")
    @GetMapping("/dashboard")
    public AjaxResult dashboard(@RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate) {
        return success(fundTrackingService.getDashboardStats(startDate, endDate));
    }

    @RequiresPermissions("plan:fund:tracking:list")
    @GetMapping("/splits/{paymentId}")
    public AjaxResult splits(@PathVariable Long paymentId) {
        return success(fundTrackingService.selectPaymentSplits(paymentId));
    }
}
