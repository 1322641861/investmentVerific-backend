package com.ruoyi.modules.invest.risk.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.risk.domain.RiskAssessment;
import com.ruoyi.modules.invest.risk.mapper.RiskAssessmentMapper;
import com.ruoyi.modules.invest.risk.service.IRiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 风险评估Service业务层
 *
 * @author investvf
 */
@Service
public class RiskAssessmentServiceImpl implements IRiskAssessmentService {

    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;

    @Override
    public RiskAssessment selectRiskAssessmentById(Long riskId) {
        return riskAssessmentMapper.selectRiskAssessmentById(riskId);
    }

    @Override
    public List<RiskAssessment> selectRiskAssessmentList(RiskAssessment riskAssessment) {
        return riskAssessmentMapper.selectRiskAssessmentList(riskAssessment);
    }

    @Override
    public int insertRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessment.setCreateBy(ShiroUtils.getLoginName());
        riskAssessment.setCreateTime(DateUtils.getNowDate());
        riskAssessment.setDelFlag("0");
        if (riskAssessment.getAssessmentStatus() == null) {
            riskAssessment.setAssessmentStatus("0");
        }
        calculateTotalScoreAndLevel(riskAssessment);
        return riskAssessmentMapper.insertRiskAssessment(riskAssessment);
    }

    @Override
    public int updateRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessment.setUpdateBy(ShiroUtils.getLoginName());
        riskAssessment.setUpdateTime(DateUtils.getNowDate());
        calculateTotalScoreAndLevel(riskAssessment);
        return riskAssessmentMapper.updateRiskAssessment(riskAssessment);
    }

    @Override
    public int deleteRiskAssessmentByIds(Long[] ids) {
        return riskAssessmentMapper.deleteRiskAssessmentByIds(ids);
    }

    @Override
    public RiskAssessment selectByProjectId(Long projectId) {
        return riskAssessmentMapper.selectByProjectId(projectId);
    }

    @Override
    public RiskAssessment calculateTotalScoreAndLevel(RiskAssessment assessment) {
        // 计算平均分
        int total = 0;
        int count = 0;
        if (assessment.getMarketRiskScore() != null) {
            total += assessment.getMarketRiskScore();
            count++;
        }
        if (assessment.getFinanceRiskScore() != null) {
            total += assessment.getFinanceRiskScore();
            count++;
        }
        if (assessment.getTechRiskScore() != null) {
            total += assessment.getTechRiskScore();
            count++;
        }
        if (assessment.getManagementRiskScore() != null) {
            total += assessment.getManagementRiskScore();
            count++;
        }
        if (assessment.getPolicyRiskScore() != null) {
            total += assessment.getPolicyRiskScore();
            count++;
        }

        if (count > 0) {
            BigDecimal avg = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
            assessment.setTotalScore(avg);

            // 根据平均分判断风险等级
            // 0-20: A低风险  21-40: B中风险  41-60: C高风险  61-100: D极高风险
            double score = avg.doubleValue();
            if (score <= 20) {
                assessment.setRiskLevel("A");
            } else if (score <= 40) {
                assessment.setRiskLevel("B");
            } else if (score <= 60) {
                assessment.setRiskLevel("C");
            } else {
                assessment.setRiskLevel("D");
            }
        }
        return assessment;
    }
}
