package com.ruoyi.modules.invest.plan.scheme.service.impl;

import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeVersionMapper;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规划方案版本Service业务层处理
 *
 * @author investvf
 */
@Service
public class PlanSchemeVersionServiceImpl implements IPlanSchemeVersionService {

    @Autowired
    private PlanSchemeVersionMapper versionMapper;

    @Override
    public PlanSchemeVersion selectPlanSchemeVersionById(Long versionId) {
        return versionMapper.selectPlanSchemeVersionById(versionId);
    }

    @Override
    public List<PlanSchemeVersion> selectPlanSchemeVersionList(PlanSchemeVersion version) {
        return versionMapper.selectPlanSchemeVersionList(version);
    }

    @Override
    public List<PlanSchemeVersion> selectVersionListBySchemeId(Long schemeId) {
        PlanSchemeVersion version = new PlanSchemeVersion();
        version.setSchemeId(schemeId);
        return versionMapper.selectPlanSchemeVersionList(version);
    }
}
