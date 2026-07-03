package com.ruoyi.modules.invest.report.data.service;

import java.util.List;
import java.util.Map;

/**
 * 标准报表数据服务
 *
 * @author investvf
 */
public interface IReportDataService {

    /** 方案汇总报表 */
    Map<String, Object> getSchemeReport(Integer planYear);

    /** 项目分析报表 */
    Map<String, Object> getProjectReport(String projectStatus);

    /** 资金分析报表 */
    Map<String, Object> getFundReport(Integer planYear);

    /** 效益分析报表 */
    Map<String, Object> getBenefitReport(Integer planYear);

    /** 进度分析报表 */
    Map<String, Object> getProgressReport(Integer planYear);
}
