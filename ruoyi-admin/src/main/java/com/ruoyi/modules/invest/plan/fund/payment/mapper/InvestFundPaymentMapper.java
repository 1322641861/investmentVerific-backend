package com.ruoyi.modules.invest.plan.fund.payment.mapper;

import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPayment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/** 资金支付Mapper接口 */
@Mapper
public interface InvestFundPaymentMapper {
    InvestFundPayment selectInvestFundPaymentById(Long paymentId);
    List<InvestFundPayment> selectInvestFundPaymentList(InvestFundPayment payment);
    int insertInvestFundPayment(InvestFundPayment payment);
    int updateInvestFundPayment(InvestFundPayment payment);
    int deleteInvestFundPaymentByIds(Long[] paymentIds);
}
