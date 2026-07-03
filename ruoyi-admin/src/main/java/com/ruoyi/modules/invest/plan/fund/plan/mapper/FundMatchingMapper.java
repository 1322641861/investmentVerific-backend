package com.ruoyi.modules.invest.plan.fund.plan.mapper;

import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingStatVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundMatchingTrendVO;
import com.ruoyi.modules.invest.plan.fund.plan.domain.dto.FundSourceMatchingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资金配套率分析Mapper
 *
 * @author investvf
 */
@Mapper
public interface FundMatchingMapper {

    /**
     * 查询配套率统计数据
     */
    List<FundMatchingStatVO> selectMatchingStatList(@Param("projectName") String projectName,
                                                    @Param("planStatus") String planStatus,
                                                    @Param("matchRateLow") String matchRateLow,
                                                    @Param("matchRateHigh") String matchRateHigh);

    /**
     * 查询配套率趋势（来自校验日志）
     */
    List<FundMatchingTrendVO> selectMatchingTrend(@Param("planId") Long planId);

    /**
     * 查询资金来源配套明细
     */
    List<FundSourceMatchingVO> selectSourceMatching(@Param("planId") Long planId);

    /**
     * 统计卡片：总计划数
     */
    Long countTotalPlans();

    /**
     * 统计卡片：平均配套率
     */
    Double avgMatchRate();

    /**
     * 统计卡片：低于阈值的计划数
     */
    Long countLowMatchRate(@Param("threshold") Double threshold);

    /**
     * 统计卡片：平均到账率
     */
    Double avgArrivedRate();
}
