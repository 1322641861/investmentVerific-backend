package com.ruoyi.modules.invest.plan.scheme.mapper;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 规划方案版本Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface PlanSchemeVersionMapper {

    PlanSchemeVersion selectPlanSchemeVersionById(Long versionId);

    List<PlanSchemeVersion> selectPlanSchemeVersionList(PlanSchemeVersion version);

    int insertPlanSchemeVersion(PlanSchemeVersion version);

    int clearCurrentBySchemeId(Long schemeId);

    Integer selectMaxVersionNo(Long schemeId);
}
