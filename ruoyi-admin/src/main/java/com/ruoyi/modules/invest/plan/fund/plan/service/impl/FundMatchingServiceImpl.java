package com.ruoyi.modules.invest.plan.fund.plan.service.impl;

import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingStatVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingTrendVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundSourceMatchingVO;
import com.ruoyi.modules.invest.plan.fund.plan.mapper.FundMatchingMapper;
import com.ruoyi.modules.invest.plan.fund.plan.service.IFundMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金配套率分析Service实现
 *
 * @author investvf
 */
@Service
public class FundMatchingServiceImpl implements IFundMatchingService {

    @Autowired
    private FundMatchingMapper matchingMapper;

    @Override
    public List<FundMatchingStatVO> selectMatchingStatList(String projectName, String planStatus,
                                                           String matchRateLow, String matchRateHigh) {
        return matchingMapper.selectMatchingStatList(projectName, planStatus, matchRateLow, matchRateHigh);
    }

    @Override
    public List<FundMatchingTrendVO> selectMatchingTrend(Long planId) {
        return matchingMapper.selectMatchingTrend(planId);
    }

    @Override
    public List<FundSourceMatchingVO> selectSourceMatching(Long planId) {
        return matchingMapper.selectSourceMatching(planId);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlans", matchingMapper.countTotalPlans());
        stats.put("avgMatchRate", matchingMapper.avgMatchRate());
        stats.put("lowMatchRateCount", matchingMapper.countLowMatchRate(80.0));
        stats.put("avgArrivedRate", matchingMapper.avgArrivedRate());
        return stats;
    }
}
