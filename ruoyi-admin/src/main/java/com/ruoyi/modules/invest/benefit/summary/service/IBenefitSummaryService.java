package com.ruoyi.modules.invest.benefit.summary.service;

import com.ruoyi.modules.invest.benefit.summary.domain.dto.BenefitSummaryVO;

import java.util.Map;

public interface IBenefitSummaryService {
    BenefitSummaryVO project(Long projectId);
    BenefitSummaryVO scheme(Long schemeId);
    Map<String, Object> getSummary();
}
