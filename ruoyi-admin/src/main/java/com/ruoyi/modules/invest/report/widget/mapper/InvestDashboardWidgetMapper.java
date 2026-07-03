package com.ruoyi.modules.invest.report.widget.mapper;

import com.ruoyi.modules.invest.report.widget.domain.InvestDashboardWidget;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestDashboardWidgetMapper {
    InvestDashboardWidget selectById(Long id);
    List<InvestDashboardWidget> selectList(InvestDashboardWidget widget);
    List<InvestDashboardWidget> selectAllEnabled();
    int insert(InvestDashboardWidget widget);
    int update(InvestDashboardWidget widget);
    int deleteByIds(Long[] ids);
}
