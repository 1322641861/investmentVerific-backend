package com.ruoyi.modules.invest.plan.scheme.mapper;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 规划方案Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface PlanSchemeMapper {

    PlanScheme selectPlanSchemeById(Long schemeId);

    List<PlanScheme> selectPlanSchemeList(PlanScheme planScheme);

    int insertPlanScheme(PlanScheme planScheme);

    int updatePlanScheme(PlanScheme planScheme);

    int deletePlanSchemeById(Long schemeId);

    int deletePlanSchemeByIds(Long[] schemeIds);

    int updateSchemeStatus(PlanScheme planScheme);
}
