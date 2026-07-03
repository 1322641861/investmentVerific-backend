package com.ruoyi.modules.invest.execution.report.mapper;

import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReviewLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvestProgressReviewLogMapper { int insert(InvestProgressReviewLog log); }
