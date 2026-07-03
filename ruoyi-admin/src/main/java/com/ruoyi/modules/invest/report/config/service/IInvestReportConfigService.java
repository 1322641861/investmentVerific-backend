package com.ruoyi.modules.invest.report.config.service;

import com.ruoyi.modules.invest.report.config.domain.InvestReportConfig;
import java.util.List;

public interface IInvestReportConfigService {
    InvestReportConfig selectById(Long id);
    List<InvestReportConfig> selectList(InvestReportConfig config);
    int insert(InvestReportConfig config);
    int update(InvestReportConfig config);
    int deleteByIds(Long[] ids);
}
