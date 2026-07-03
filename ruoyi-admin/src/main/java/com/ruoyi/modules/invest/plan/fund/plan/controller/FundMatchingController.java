package com.ruoyi.modules.invest.plan.fund.plan.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingStatVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingTrendVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundSourceMatchingVO;
import com.ruoyi.modules.invest.plan.fund.plan.service.IFundMatchingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资金配套率分析Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/plan/fund/matching")
public class FundMatchingController extends BaseController {

    @Autowired
    private IFundMatchingService fundMatchingService;

    /**
     * 查询配套率汇总列表
     */
    @RequiresPermissions("plan:fund:matching:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String projectName,
                              @RequestParam(required = false) String planStatus,
                              @RequestParam(required = false) String matchRateLow,
                              @RequestParam(required = false) String matchRateHigh) {
        startPage();
        List<FundMatchingStatVO> list = fundMatchingService.selectMatchingStatList(
                projectName, planStatus, matchRateLow, matchRateHigh);
        return getDataTable(list);
    }

    /**
     * 查询配套率趋势
     */
    @RequiresPermissions("plan:fund:matching:list")
    @GetMapping("/trend/{planId}")
    public AjaxResult trend(@PathVariable Long planId) {
        List<FundMatchingTrendVO> list = fundMatchingService.selectMatchingTrend(planId);
        return success(list);
    }

    /**
     * 查询资金来源配套明细
     */
    @RequiresPermissions("plan:fund:matching:list")
    @GetMapping("/source/{planId}")
    public AjaxResult source(@PathVariable Long planId) {
        List<FundSourceMatchingVO> list = fundMatchingService.selectSourceMatching(planId);
        return success(list);
    }

    /**
     * 获取统计卡片数据
     */
    @RequiresPermissions("plan:fund:matching:list")
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        Map<String, Object> stats = fundMatchingService.getDashboardStats();
        return success(stats);
    }
}
