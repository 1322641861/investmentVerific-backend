package com.ruoyi.modules.invest.plan.fund.payment.service;

import com.ruoyi.modules.invest.plan.fund.payment.domain.InvestFundPayment;
import java.util.List;

public interface IInvestFundPaymentService {
    InvestFundPayment selectInvestFundPaymentById(Long paymentId);
    List<InvestFundPayment> selectInvestFundPaymentList(InvestFundPayment payment);
    int insertInvestFundPayment(InvestFundPayment payment);
    int updateInvestFundPayment(InvestFundPayment payment);
    int deleteInvestFundPaymentByIds(Long[] paymentIds);
    boolean submitPayment(Long paymentId);
    boolean payPayment(Long paymentId);
    boolean writeOffPayment(Long paymentId);
}
