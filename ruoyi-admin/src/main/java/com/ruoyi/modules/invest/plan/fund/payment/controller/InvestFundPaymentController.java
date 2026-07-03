package com.ruoyi.modules.invest.plan.fund.payment.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPayment;
import com.ruoyi.modules.invest.plan.fund.payment.service.IInvestFundPaymentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/plan/fund/payment")
public class InvestFundPaymentController extends BaseController {
    @Autowired private IInvestFundPaymentService service;
    @RequiresPermissions("plan:fund:payment:list") @GetMapping("/list") public TableDataInfo list(InvestFundPayment p){startPage(); List<InvestFundPayment> list=service.selectInvestFundPaymentList(p); return getDataTable(list);}
    @RequiresPermissions("plan:fund:payment:list") @GetMapping("/{paymentId}") public AjaxResult get(@PathVariable Long paymentId){return success(service.selectInvestFundPaymentById(paymentId));}
    @RequiresPermissions("plan:fund:payment:add") @Log(title="支付申请",businessType= BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody InvestFundPayment p){return toAjax(service.insertInvestFundPayment(p));}
    @RequiresPermissions("plan:fund:payment:edit") @Log(title="支付申请",businessType= BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody InvestFundPayment p){return toAjax(service.updateInvestFundPayment(p));}
    @RequiresPermissions("plan:fund:payment:remove") @Log(title="支付申请",businessType= BusinessType.DELETE) @DeleteMapping("/{paymentIds}") public AjaxResult remove(@PathVariable Long[] paymentIds){return toAjax(service.deleteInvestFundPaymentByIds(paymentIds));}
    @RequiresPermissions("plan:fund:payment:submit") @Log(title="支付申请提交",businessType= BusinessType.UPDATE) @PostMapping("/submit/{paymentId}") public AjaxResult submit(@PathVariable Long paymentId){return toAjax(service.submitPayment(paymentId));}
    @RequiresPermissions("plan:fund:payment:pay") @Log(title="支付确认",businessType= BusinessType.UPDATE) @PostMapping("/pay/{paymentId}") public AjaxResult pay(@PathVariable Long paymentId){return toAjax(service.payPayment(paymentId));}
    @RequiresPermissions("plan:fund:payment:edit") @Log(title="支付核销",businessType= BusinessType.UPDATE) @PostMapping("/write-off/{paymentId}") public AjaxResult writeOff(@PathVariable Long paymentId){return toAjax(service.writeOffPayment(paymentId));}
}
