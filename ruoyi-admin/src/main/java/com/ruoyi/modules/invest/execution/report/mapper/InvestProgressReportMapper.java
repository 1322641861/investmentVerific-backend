package com.ruoyi.modules.invest.execution.report.mapper;

import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReport;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestProgressReportMapper { InvestProgressReport selectById(Long reportId); List<InvestProgressReport> selectList(InvestProgressReport r); int insert(InvestProgressReport r); int update(InvestProgressReport r); int deleteByIds(Long[] ids); }
