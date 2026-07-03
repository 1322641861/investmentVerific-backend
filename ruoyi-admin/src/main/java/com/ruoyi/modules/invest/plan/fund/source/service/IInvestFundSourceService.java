package com.ruoyi.modules.invest.plan.fund.source.service;

import com.ruoyi.modules.invest.plan.fund.source.domain.InvestFundSource;
import java.util.List;

public interface IInvestFundSourceService {
    InvestFundSource selectInvestFundSourceById(Long sourceId);
    List<InvestFundSource> selectInvestFundSourceList(InvestFundSource source);
    int insertInvestFundSource(InvestFundSource source);
    int updateInvestFundSource(InvestFundSource source);
    int deleteInvestFundSourceByIds(Long[] sourceIds);
}
