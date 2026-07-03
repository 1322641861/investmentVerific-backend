package com.ruoyi.modules.invest.plan.scheme.mapper;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTagRel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 规划方案标签关联Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface PlanSchemeTagRelMapper {

    List<PlanSchemeTagRel> selectPlanSchemeTagRelBySchemeId(Long schemeId);

    int insertPlanSchemeTagRel(PlanSchemeTagRel rel);

    int deleteBySchemeId(Long schemeId);

    int deleteByTagIds(Long[] tagIds);
}
