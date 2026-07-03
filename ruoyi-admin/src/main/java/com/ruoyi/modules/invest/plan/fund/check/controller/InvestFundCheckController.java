package com.ruoyi.modules.invest.plan.fund.check.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.modules.invest.plan.fund.check.domain.InvestFundCheckLog;
import com.ruoyi.modules.invest.plan.fund.check.service.IInvestFundCheckLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/plan/fund/check")
public class InvestFundCheckController extends BaseController {
    @Autowired private IInvestFundCheckLogService service;
    @RequiresPermissions("plan:fund:check:list") @GetMapping("/list") public TableDataInfo list(InvestFundCheckLog log){startPage(); List<InvestFundCheckLog> list=service.selectInvestFundCheckLogList(log); return getDataTable(list);}
}
