package com.ruoyi.modules.invest.report.widget.service;

import com.ruoyi.modules.invest.report.widget.domain.InvestDashboardWidget;
import java.util.List;

public interface IInvestDashboardWidgetService {
    InvestDashboardWidget selectById(Long id);
    List<InvestDashboardWidget> selectList(InvestDashboardWidget widget);
    List<InvestDashboardWidget> selectAllEnabled();
    int insert(InvestDashboardWidget widget);
    int update(InvestDashboardWidget widget);
    int deleteByIds(Long[] ids);
}
