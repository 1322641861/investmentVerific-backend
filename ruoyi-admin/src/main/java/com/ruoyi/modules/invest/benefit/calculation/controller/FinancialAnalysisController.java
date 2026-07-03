package com.ruoyi.modules.invest.benefit.calculation.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.benefit.calculation.service.IFinancialAnalysisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invest/benefit/financial")
public class FinancialAnalysisController extends BaseController {

    @Autowired
    private IFinancialAnalysisService financialAnalysisService;

    @RequiresPermissions("invest:benefit:financial:list")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) Integer planYear) {
        startPage();
        List<Map<String, Object>> list = financialAnalysisService.selectFinancialList(planYear);
        return getDataTable(list);
    }

    @RequiresPermissions("invest:benefit:financial:list")
    @GetMapping("/calculate/{calcId}")
    public AjaxResult calculate(@PathVariable Long calcId,
                                @RequestParam(required = false, defaultValue = "8") Double discountRate) {
        return success(financialAnalysisService.calculateNpvIrr(calcId, discountRate));
    }

    @RequiresPermissions("invest:benefit:financial:list")
    @GetMapping("/batch")
    public AjaxResult batch(@RequestParam(required = false, defaultValue = "8") Double discountRate) {
        return success(financialAnalysisService.batchCalculate(discountRate));
    }
}
