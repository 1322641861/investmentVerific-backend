package com.ruoyi.modules.invest.plan.fund.payment.mapper;

import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPaymentSplit;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金支付拆分Mapper接口 */
@Mapper
public interface InvestFundPaymentSplitMapper {
    List<InvestFundPaymentSplit> selectSplitsByPaymentId(Long paymentId);
    int insertInvestFundPaymentSplit(InvestFundPaymentSplit split);
    int deleteByPaymentId(Long paymentId);
}
