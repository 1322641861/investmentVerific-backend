package com.ruoyi.modules.invest.benefit.summary.service.impl;

import com.ruoyi.modules.invest.benefit.calculation.domain.InvestBenefitCalculation;
import com.ruoyi.modules.invest.benefit.calculation.mapper.InvestBenefitCalculationMapper;
import com.ruoyi.modules.invest.benefit.summary.domain.dto.BenefitSummaryVO;
import com.ruoyi.modules.invest.benefit.summary.service.IBenefitSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BenefitSummaryServiceImpl implements IBenefitSummaryService {

    @Autowired
    private InvestBenefitCalculationMapper calculationMapper;

    @Override
    public BenefitSummaryVO project(Long projectId) {
        BenefitSummaryVO v = new BenefitSummaryVO();
        v.setProjectId(projectId);
        v.setRoi(BigDecimal.ZERO);
        v.setScore(BigDecimal.ZERO);
        v.setSummary("项目效益汇总");
        return v;
    }

    @Override
    public BenefitSummaryVO scheme(Long schemeId) {
        BenefitSummaryVO v = new BenefitSummaryVO();
        v.setSchemeId(schemeId);
        v.setRoi(BigDecimal.ZERO);
        v.setScore(BigDecimal.ZERO);
        v.setSummary("方案效益汇总");
        return v;
    }

    @Override
    public Map<String, Object> getSummary() {
        Map<String, Object> result = new HashMap<>();

        InvestBenefitCalculation query = new InvestBenefitCalculation();
        List<InvestBenefitCalculation> calculations = calculationMapper.selectList(query);

        int totalProject = calculations.size();
        BigDecimal totalInvest = BigDecimal.ZERO;
        BigDecimal totalRoi = BigDecimal.ZERO;
        BigDecimal totalScore = BigDecimal.ZERO;

        for (InvestBenefitCalculation c : calculations) {
            if (c.getInvestAmount() != null) {
                totalInvest = totalInvest.add(c.getInvestAmount());
            }
            if (c.getRoi() != null) {
                totalRoi = totalRoi.add(c.getRoi());
            }
            if (c.getBenefitScore() != null) {
                totalScore = totalScore.add(c.getBenefitScore());
            }
        }

        BigDecimal avgRoi = totalProject > 0 ? totalRoi.divide(new BigDecimal(totalProject), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal avgScore = totalProject > 0 ? totalScore.divide(new BigDecimal(totalProject), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        result.put("totalProject", totalProject);
        result.put("totalInvest", totalInvest);
        result.put("avgRoi", avgRoi);
        result.put("avgScore", avgScore);

        return result;
    }
}
