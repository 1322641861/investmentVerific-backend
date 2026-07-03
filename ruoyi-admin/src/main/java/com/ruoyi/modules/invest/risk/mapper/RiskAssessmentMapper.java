package com.ruoyi.modules.invest.risk.mapper;

import com.ruoyi.modules.invest.risk.domain.RiskAssessment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 风险评估Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface RiskAssessmentMapper {

    RiskAssessment selectRiskAssessmentById(Long riskId);

    List<RiskAssessment> selectRiskAssessmentList(RiskAssessment riskAssessment);

    int insertRiskAssessment(RiskAssessment riskAssessment);

    int updateRiskAssessment(RiskAssessment riskAssessment);

    int deleteRiskAssessmentByIds(Long[] ids);

    RiskAssessment selectByProjectId(Long projectId);
}
