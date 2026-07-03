package com.ruoyi.modules.invest.execution.report.service;

import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReport;
import com.ruoyi.modules.invest.execution.report.domain.dto.ProgressReviewRequest;
import java.util.List;

public interface IInvestProgressReportService { InvestProgressReport selectById(Long id); List<InvestProgressReport> selectList(InvestProgressReport r); int insert(InvestProgressReport r); int update(InvestProgressReport r); int deleteByIds(Long[] ids); boolean submit(Long id); boolean withdraw(Long id); boolean review(Long id, ProgressReviewRequest req); }
