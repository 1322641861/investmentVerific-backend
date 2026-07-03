package com.ruoyi.modules.invest.report.config.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.report.config.domain.InvestReportConfig;
import com.ruoyi.modules.invest.report.config.mapper.InvestReportConfigMapper;
import com.ruoyi.modules.invest.report.config.service.IInvestReportConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestReportConfigServiceImpl implements IInvestReportConfigService {
    @Autowired
    private InvestReportConfigMapper mapper;

    public InvestReportConfig selectById(Long id) { return mapper.selectById(id); }
    public List<InvestReportConfig> selectList(InvestReportConfig config) { return mapper.selectList(config); }
    public int insert(InvestReportConfig config) {
        config.setCreateBy(ShiroUtils.getLoginName());
        config.setCreateTime(DateUtils.getNowDate());
        config.setDelFlag("0");
        if(config.getStatus()==null) config.setStatus("0");
        if(config.getRefreshInterval()==null) config.setRefreshInterval(300);
        return mapper.insert(config);
    }
    public int update(InvestReportConfig config) {
        config.setUpdateBy(ShiroUtils.getLoginName());
        config.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(config);
    }
    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
}
