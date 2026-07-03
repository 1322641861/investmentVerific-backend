package com.ruoyi.modules.invest.plan.fund.source.mapper;

import com.ruoyi.modules.invest.plan.fund.source.domain.InvestFundSource;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金来源Mapper接口 */
@Mapper
public interface InvestFundSourceMapper {
    InvestFundSource selectInvestFundSourceById(Long sourceId);
    List<InvestFundSource> selectInvestFundSourceList(InvestFundSource source);
    int insertInvestFundSource(InvestFundSource source);
    int updateInvestFundSource(InvestFundSource source);
    int deleteInvestFundSourceByIds(Long[] sourceIds);
}
