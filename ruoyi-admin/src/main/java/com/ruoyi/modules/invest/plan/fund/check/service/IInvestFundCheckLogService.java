package com.ruoyi.modules.invest.plan.fund.check.service;

import com.ruoyi.modules.invest.plan.fund.check.domain.InvestFundCheckLog;
import java.util.List;

public interface IInvestFundCheckLogService {
    List<InvestFundCheckLog> selectInvestFundCheckLogList(InvestFundCheckLog log);
    List<InvestFundCheckLog> selectLogsByPlanId(Long planId);
}
