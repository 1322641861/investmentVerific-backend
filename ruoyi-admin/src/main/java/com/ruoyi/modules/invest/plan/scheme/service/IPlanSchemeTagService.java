package com.ruoyi.modules.invest.plan.scheme.service;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTag;

import java.util.List;

/**
 * 规划方案标签Service接口
 *
 * @author investvf
 */
public interface IPlanSchemeTagService {

    PlanSchemeTag selectPlanSchemeTagById(Long tagId);

    List<PlanSchemeTag> selectPlanSchemeTagList(PlanSchemeTag tag);

    List<PlanSchemeTag> selectTagsBySchemeId(Long schemeId);

    int insertPlanSchemeTag(PlanSchemeTag tag);

    int updatePlanSchemeTag(PlanSchemeTag tag);

    int deletePlanSchemeTagByIds(Long[] tagIds);
}
