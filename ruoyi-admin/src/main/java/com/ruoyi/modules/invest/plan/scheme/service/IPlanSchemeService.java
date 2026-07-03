package com.ruoyi.modules.invest.plan.scheme.service;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;

import java.util.List;
import java.util.Map;

/**
 * 规划方案Service接口
 *
 * @author investvf
 */
public interface IPlanSchemeService {

    PlanScheme selectPlanSchemeById(Long schemeId);

    List<PlanScheme> selectPlanSchemeList(PlanScheme planScheme);

    int insertPlanScheme(PlanScheme planScheme);

    int updatePlanScheme(PlanScheme planScheme);

    int deletePlanSchemeByIds(Long[] schemeIds);

    int deletePlanSchemeById(Long schemeId);

    boolean submitApproval(Long schemeId);

    PlanSchemeVersion createVersion(Long schemeId, String changeLog);

    boolean rollbackVersion(Long versionId);

    List<Map<String, Object>> compareVersions(Long versionId1, Long versionId2);

    int bindTags(Long schemeId, Long[] tagIds);
}
