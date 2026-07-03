package com.ruoyi.modules.invest.plan.fund.plan.service;

import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingStatVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingTrendVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundSourceMatchingVO;

import java.util.List;
import java.util.Map;

/**
 * 资金配套率分析Service接口
 *
 * @author investvf
 */
public interface IFundMatchingService {

    /**
     * 查询配套率汇总列表
     */
    List<FundMatchingStatVO> selectMatchingStatList(String projectName, String planStatus,
                                                    String matchRateLow, String matchRateHigh);

    /**
     * 查询配套率趋势
     */
    List<FundMatchingTrendVO> selectMatchingTrend(Long planId);

    /**
     * 查询资金来源配套明细
     */
    List<FundSourceMatchingVO> selectSourceMatching(Long planId);

    /**
     * 获取统计卡片数据
     */
    Map<String, Object> getDashboardStats();
}
