package com.ruoyi.modules.invest.plan.fund.plan.mapper;

import com.ruoyi.modules.invest.plan.fund.plan.domain.InvestFundPlanItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金配套计划明细Mapper接口 */
@Mapper
public interface InvestFundPlanItemMapper {
    InvestFundPlanItem selectInvestFundPlanItemById(Long itemId);
    List<InvestFundPlanItem> selectItemsByPlanId(Long planId);
    int insertInvestFundPlanItem(InvestFundPlanItem item);
    int updateInvestFundPlanItem(InvestFundPlanItem item);
    int deleteByPlanId(Long planId);
    int addArrivedAmount(InvestFundPlanItem item);
    int addPaidAmount(InvestFundPlanItem item);
}
