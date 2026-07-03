package com.ruoyi.modules.invest.plan.budget.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationQuery;
import com.ruoyi.modules.invest.plan.budget.domain.dto.BudgetDeviationVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.MonthlyTrendVO;
import com.ruoyi.modules.invest.plan.budget.domain.dto.ProjectDeviationVO;
import com.ruoyi.modules.invest.plan.budget.service.IBudgetDeviationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预算执行偏差分析Controller
 *
 * @author investvf
 */
@RestController
@RequestMapping("/plan/budget/deviation")
public class BudgetDeviationController extends BaseController {

    @Autowired
    private IBudgetDeviationService budgetDeviationService;

    /**
     * 查询预算执行偏差汇总列表
     */
    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/list")
    public TableDataInfo list(BudgetDeviationQuery query) {
        startPage();
        List<BudgetDeviationVO> list = budgetDeviationService.selectDeviationList(query);
        return getDataTable(list);
    }

    /**
     * 查询单个预算的汇总偏差
     */
    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/summary/{budgetId}")
    public AjaxResult summary(@PathVariable Long budgetId) {
        BudgetDeviationVO vo = budgetDeviationService.selectDeviationSummary(budgetId);
        return success(vo);
    }

    /**
     * 查询项目级偏差明细
     */
    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/detail/{budgetId}")
    public AjaxResult detail(@PathVariable Long budgetId) {
        List<ProjectDeviationVO> list = budgetDeviationService.selectProjectDeviation(budgetId);
        return success(list);
    }

    /**
     * 查询月度执行趋势
     */
    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/trend/{budgetId}/{year}")
    public AjaxResult trend(@PathVariable Long budgetId, @PathVariable Integer year) {
        List<MonthlyTrendVO> list = budgetDeviationService.selectMonthlyTrend(budgetId, year);
        return success(list);
    }

    /**
     * 按年度统计汇总偏差
     */
    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/summary-by-year")
    public TableDataInfo summaryByYear(@RequestParam(required = false) Integer planYear) {
        startPage();
        List<BudgetDeviationVO> list = budgetDeviationService.selectDeviationSummaryByYear(planYear);
        return getDataTable(list);
    }

    @RequiresPermissions("plan:budget:deviation:list")
    @GetMapping("/by-dept")
    public AjaxResult byDept() {
        return success(budgetDeviationService.selectDeviationByDept());
    }
}
