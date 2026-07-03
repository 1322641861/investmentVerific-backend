package com.ruoyi.modules.invest.plan.fund.check.service.impl;

import com.ruoyi.modules.invest.plan.fund.check.domain.InvestFundCheckLog;
import com.ruoyi.modules.invest.plan.fund.check.mapper.InvestFundCheckLogMapper;
import com.ruoyi.modules.invest.plan.fund.check.service.IInvestFundCheckLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestFundCheckLogServiceImpl implements IInvestFundCheckLogService {
    @Autowired private InvestFundCheckLogMapper mapper;
    public List<InvestFundCheckLog> selectInvestFundCheckLogList(InvestFundCheckLog log){return mapper.selectInvestFundCheckLogList(log);}
    public List<InvestFundCheckLog> selectLogsByPlanId(Long planId){return mapper.selectLogsByPlanId(planId);}
}
