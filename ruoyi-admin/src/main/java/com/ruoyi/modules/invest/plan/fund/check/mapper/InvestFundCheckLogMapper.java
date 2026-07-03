package com.ruoyi.modules.invest.plan.fund.check.mapper;

import com.ruoyi.modules.invest.plan.fund.check.domain.InvestFundCheckLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金配套校验记录Mapper接口 */
@Mapper
public interface InvestFundCheckLogMapper {
    InvestFundCheckLog selectInvestFundCheckLogById(Long logId);
    List<InvestFundCheckLog> selectInvestFundCheckLogList(InvestFundCheckLog log);
    List<InvestFundCheckLog> selectLogsByPlanId(Long planId);
    int insertInvestFundCheckLog(InvestFundCheckLog log);
}
