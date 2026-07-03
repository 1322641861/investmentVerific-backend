package com.ruoyi.modules.invest.plan.fund.arrival.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.fund.arrival.domain.InvestFundArrival;
import com.ruoyi.modules.invest.plan.fund.arrival.service.IInvestFundArrivalService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/plan/fund/arrival")
public class InvestFundArrivalController extends BaseController {
    @Autowired private IInvestFundArrivalService service;
    @RequiresPermissions("plan:fund:arrival:list") @GetMapping("/list") public TableDataInfo list(InvestFundArrival a){startPage(); List<InvestFundArrival> list=service.selectInvestFundArrivalList(a); return getDataTable(list);}
    @RequiresPermissions("plan:fund:arrival:list") @GetMapping("/{arrivalId}") public AjaxResult get(@PathVariable Long arrivalId){return success(service.selectInvestFundArrivalById(arrivalId));}
    @RequiresPermissions("plan:fund:arrival:add") @Log(title="到账登记",businessType= BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestFundArrival a){return toAjax(service.insertInvestFundArrival(a));}
    @RequiresPermissions("plan:fund:arrival:edit") @Log(title="到账登记",businessType= BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestFundArrival a){return toAjax(service.updateInvestFundArrival(a));}
    @RequiresPermissions("plan:fund:arrival:void") @Log(title="到账冲销",businessType= BusinessType.UPDATE) @PostMapping("/void/{arrivalId}") public AjaxResult voidArrival(@PathVariable Long arrivalId){return toAjax(service.voidArrival(arrivalId));}
    @RequiresPermissions("plan:fund:arrival:edit") @Log(title="到账核验",businessType= BusinessType.UPDATE) @PostMapping("/verify/{arrivalId}") public AjaxResult verify(@PathVariable Long arrivalId, @RequestParam(required=false) String voucherNo, @RequestParam(required=false) String attachment){return toAjax(service.verifyArrival(arrivalId, voucherNo, attachment));}
}
