package com.ruoyi.modules.invest.plan.fund.arrival.service;

import com.ruoyi.modules.invest.plan.fund.arrival.domain.InvestFundArrival;
import java.util.List;

public interface IInvestFundArrivalService {
    InvestFundArrival selectInvestFundArrivalById(Long arrivalId);
    List<InvestFundArrival> selectInvestFundArrivalList(InvestFundArrival arrival);
    int insertInvestFundArrival(InvestFundArrival arrival);
    int updateInvestFundArrival(InvestFundArrival arrival);
    int voidArrival(Long arrivalId);
    int verifyArrival(Long arrivalId, String voucherNo, String attachment);
}
