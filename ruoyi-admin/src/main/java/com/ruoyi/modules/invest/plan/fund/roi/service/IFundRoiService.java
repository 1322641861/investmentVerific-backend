package com.ruoyi.modules.invest.plan.fund.roi.service;

import java.util.List;
import java.util.Map;

public interface IFundRoiService {
    Map<String, Object> getDashboard();
    List<Map<String, Object>> selectProjectRoiList(Integer planYear, String orderBy);
    List<Map<String, Object>> selectMonthlyTrend(Integer year);
}
