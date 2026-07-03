package com.ruoyi.modules.invest.report.config.mapper;

import com.ruoyi.modules.invest.report.config.domain.InvestReportConfig;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestReportConfigMapper {
    InvestReportConfig selectById(Long id);
    List<InvestReportConfig> selectList(InvestReportConfig config);
    int insert(InvestReportConfig config);
    int update(InvestReportConfig config);
    int deleteByIds(Long[] ids);
}
