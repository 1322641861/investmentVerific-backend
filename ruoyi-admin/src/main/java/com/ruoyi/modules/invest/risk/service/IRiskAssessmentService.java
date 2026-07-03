package com.ruoyi.modules.invest.risk.service;

import com.ruoyi.modules.invest.risk.domain.RiskAssessment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 风险评估Service接口
 *
 * @author investvf
 */
public interface IRiskAssessmentService {

    RiskAssessment selectRiskAssessmentById(Long riskId);

    List<RiskAssessment> selectRiskAssessmentList(RiskAssessment riskAssessment);

    int insertRiskAssessment(RiskAssessment riskAssessment);

    int updateRiskAssessment(RiskAssessment riskAssessment);

    int deleteRiskAssessmentByIds(Long[] ids);

    RiskAssessment selectByProjectId(Long projectId);

    /**
     * 计算总分和风险等级
     *
     * @param assessment 风险评估对象
     * @return 计算后的对象
     */
    RiskAssessment calculateTotalScoreAndLevel(RiskAssessment assessment);
}
