package com.ruoyi.modules.invest.plan.scheme.service;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;

import java.util.List;

/**
 * 规划方案版本Service接口
 *
 * @author investvf
 */
public interface IPlanSchemeVersionService {

    PlanSchemeVersion selectPlanSchemeVersionById(Long versionId);

    List<PlanSchemeVersion> selectPlanSchemeVersionList(PlanSchemeVersion version);

    List<PlanSchemeVersion> selectVersionListBySchemeId(Long schemeId);
}
