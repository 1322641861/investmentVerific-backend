package com.ruoyi.modules.invest.report.widget.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.report.widget.domain.InvestDashboardWidget;
import com.ruoyi.modules.invest.report.widget.mapper.InvestDashboardWidgetMapper;
import com.ruoyi.modules.invest.report.widget.service.IInvestDashboardWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestDashboardWidgetServiceImpl implements IInvestDashboardWidgetService {
    @Autowired
    private InvestDashboardWidgetMapper mapper;

    public InvestDashboardWidget selectById(Long id) { return mapper.selectById(id); }
    public List<InvestDashboardWidget> selectList(InvestDashboardWidget widget) { return mapper.selectList(widget); }
    public List<InvestDashboardWidget> selectAllEnabled() { return mapper.selectAllEnabled(); }
    public int insert(InvestDashboardWidget widget) {
        widget.setCreateBy(ShiroUtils.getLoginName());
        widget.setCreateTime(DateUtils.getNowDate());
        widget.setDelFlag("0");
        if(widget.getStatus()==null) widget.setStatus("0");
        if(widget.getWidth()==null) widget.setWidth(12);
        if(widget.getHeight()==null) widget.setHeight(200);
        return mapper.insert(widget);
    }
    public int update(InvestDashboardWidget widget) {
        widget.setUpdateBy(ShiroUtils.getLoginName());
        widget.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(widget);
    }
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
