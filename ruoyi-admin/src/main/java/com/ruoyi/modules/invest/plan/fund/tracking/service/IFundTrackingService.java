package com.ruoyi.modules.invest.plan.fund.tracking.service;

import java.util.List;
import java.util.Map;

/**
 * 资金去向跟踪Service
 *
 * @author investvf
 */
public interface IFundTrackingService {

    /** 查询支付台账列表 */
    List<Map<String, Object>> selectPaymentLedger(String projectName, String payeeName,
                                                   String sourceName, String startDate,
                                                   String endDate);

    /** 统计卡片 */
    Map<String, Object> getDashboardStats(String startDate, String endDate);

    /** 查询单笔支付的资金来源拆分 */
    List<Map<String, Object>> selectPaymentSplits(Long paymentId);
}
