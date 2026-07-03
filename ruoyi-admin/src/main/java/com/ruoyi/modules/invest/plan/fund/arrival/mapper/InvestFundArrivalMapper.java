package com.ruoyi.modules.invest.plan.fund.arrival.mapper;

import com.ruoyi.modules.invest.plan.fund.arrival.domain.InvestFundArrival;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金到账Mapper接口 */
@Mapper
public interface InvestFundArrivalMapper {
    InvestFundArrival selectInvestFundArrivalById(Long arrivalId);
    List<InvestFundArrival> selectInvestFundArrivalList(InvestFundArrival arrival);
    int insertInvestFundArrival(InvestFundArrival arrival);
    int updateInvestFundArrival(InvestFundArrival arrival);
    int voidArrival(Long arrivalId);
}