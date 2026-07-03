package com.ruoyi.modules.invest.plan.scheme.mapper;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 规划方案标签Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface PlanSchemeTagMapper {

    PlanSchemeTag selectPlanSchemeTagById(Long tagId);

    List<PlanSchemeTag> selectPlanSchemeTagList(PlanSchemeTag tag);

    List<PlanSchemeTag> selectTagsBySchemeId(Long schemeId);

    int insertPlanSchemeTag(PlanSchemeTag tag);

    int updatePlanSchemeTag(PlanSchemeTag tag);

    int deletePlanSchemeTagByIds(Long[] tagIds);
}
