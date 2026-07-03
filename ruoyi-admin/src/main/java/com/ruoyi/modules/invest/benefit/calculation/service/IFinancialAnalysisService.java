package com.ruoyi.modules.invest.benefit.calculation.service;

import java.util.List;
import java.util.Map;

/**
 * 高级财务指标分析（NPV/IRR/动态回收期）
 *
 * @author investvf
 */
public interface IFinancialAnalysisService {

    /**
     * 计算单个效益测算的NPV/IRR
     * @param calcId 效益测算ID
     * @param discountRate 折现率（百分比，如8表示8%）
     */
    Map<String, Object> calculateNpvIrr(Long calcId, Double discountRate);

    /**
     * 批量计算所有项目的财务指标
     */
    Map<String, Object> batchCalculate(Double discountRate);

    /**
     * 获取财务指标列表
     */
    List<Map<String, Object>> selectFinancialList(Integer planYear);
}
