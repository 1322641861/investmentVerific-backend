package com.ruoyi.modules.invest.plan.scheme.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanScheme;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTagRel;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeVersion;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeMapper;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeTagRelMapper;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeVersionMapper;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeService;
import com.ruoyi.modules.invest.workflow.BpmnProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规划方案Service业务层处理
 *
 * @author investvf
 */
@Service
public class PlanSchemeServiceImpl implements IPlanSchemeService {

    @Autowired
    private PlanSchemeMapper planSchemeMapper;

    @Autowired
    private PlanSchemeVersionMapper versionMapper;

    @Autowired
    private PlanSchemeTagRelMapper tagRelMapper;

    @Autowired
    private BpmnProcessService bpmnProcessService;

    @Override
    public PlanScheme selectPlanSchemeById(Long schemeId) {
        return planSchemeMapper.selectPlanSchemeById(schemeId);
    }

    @Override
    public List<PlanScheme> selectPlanSchemeList(PlanScheme planScheme) {
        return planSchemeMapper.selectPlanSchemeList(planScheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPlanScheme(PlanScheme planScheme) {
        planScheme.setCreateBy(ShiroUtils.getLoginName());
        planScheme.setCreateTime(DateUtils.getNowDate());
        planScheme.setDelFlag("0");
        if (planScheme.getSchemeStatus() == null) {
            planScheme.setSchemeStatus("0");
        }
        if (planScheme.getVersionNo() == null) {
            planScheme.setVersionNo(1);
        }
        int rows = planSchemeMapper.insertPlanScheme(planScheme);
        if (rows > 0 && planScheme.getTagIds() != null) {
            bindTags(planScheme.getSchemeId(), planScheme.getTagIds());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePlanScheme(PlanScheme planScheme) {
        planScheme.setUpdateBy(ShiroUtils.getLoginName());
        planScheme.setUpdateTime(DateUtils.getNowDate());
        int rows = planSchemeMapper.updatePlanScheme(planScheme);
        if (planScheme.getSchemeId() != null && planScheme.getTagIds() != null) {
            bindTags(planScheme.getSchemeId(), planScheme.getTagIds());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlanSchemeByIds(Long[] schemeIds) {
        for (Long schemeId : schemeIds) {
            tagRelMapper.deleteBySchemeId(schemeId);
        }
        return planSchemeMapper.deletePlanSchemeByIds(schemeIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlanSchemeById(Long schemeId) {
        tagRelMapper.deleteBySchemeId(schemeId);
        return planSchemeMapper.deletePlanSchemeById(schemeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitApproval(Long schemeId) {
        PlanScheme planScheme = selectPlanSchemeById(schemeId);
        if (planScheme == null) {
            return false;
        }
        if (!"0".equals(planScheme.getSchemeStatus()) && !"3".equals(planScheme.getSchemeStatus())) {
            return false;
        }
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("schemeId", schemeId);
            variables.put("schemeName", planScheme.getSchemeName());
            variables.put("initiatorId", ShiroUtils.getUserId());
            variables.put("initiatorName", ShiroUtils.getSysUser().getUserName());
            variables.put("totalAmount", planScheme.getTotalAmount());
            ProcessInstance processInstance = bpmnProcessService.startProcess("plan_scheme_approval", schemeId.toString(), variables);
            planScheme.setProcessInstanceId(processInstance.getId());
        } catch (Exception e) {
            // 本阶段允许未部署规划方案审批流程时仍将状态置为审批中，后续补齐BPMN后再严格校验。
            planScheme.setProcessInstanceId(null);
        }
        planScheme.setSchemeStatus("1");
        planScheme.setUpdateBy(ShiroUtils.getLoginName());
        planScheme.setUpdateTime(DateUtils.getNowDate());
        return planSchemeMapper.updateSchemeStatus(planScheme) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlanSchemeVersion createVersion(Long schemeId, String changeLog) {
        PlanScheme planScheme = selectPlanSchemeById(schemeId);
        if (planScheme == null) {
            return null;
        }
        Integer maxVersionNo = versionMapper.selectMaxVersionNo(schemeId);
        int nextVersionNo = maxVersionNo == null ? 1 : maxVersionNo + 1;
        versionMapper.clearCurrentBySchemeId(schemeId);

        PlanSchemeVersion version = new PlanSchemeVersion();
        version.setSchemeId(schemeId);
        version.setVersionNo(nextVersionNo);
        version.setChangeLog(changeLog);
        version.setSnapshotJson(JSON.toJSONString(planScheme));
        version.setIsCurrent("1");
        version.setCreateBy(ShiroUtils.getLoginName());
        version.setCreateTime(DateUtils.getNowDate());
        versionMapper.insertPlanSchemeVersion(version);

        planScheme.setVersionNo(nextVersionNo);
        planScheme.setUpdateBy(ShiroUtils.getLoginName());
        planScheme.setUpdateTime(DateUtils.getNowDate());
        planSchemeMapper.updatePlanScheme(planScheme);
        return version;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindTags(Long schemeId, Long[] tagIds) {
        tagRelMapper.deleteBySchemeId(schemeId);
        if (tagIds == null || tagIds.length == 0) {
            return 1;
        }
        int rows = 0;
        for (Long tagId : tagIds) {
            PlanSchemeTagRel rel = new PlanSchemeTagRel();
            rel.setSchemeId(schemeId);
            rel.setTagId(tagId);
            rows += tagRelMapper.insertPlanSchemeTagRel(rel);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollbackVersion(Long versionId) {
        PlanSchemeVersion version = versionMapper.selectPlanSchemeVersionById(versionId);
        if (version == null || version.getSnapshotJson() == null) return false;

        PlanScheme scheme = JSON.parseObject(version.getSnapshotJson(), PlanScheme.class);
        if (scheme == null) return false;

        scheme.setUpdateBy(ShiroUtils.getLoginName());
        scheme.setUpdateTime(DateUtils.getNowDate());
        scheme.setVersionNo(version.getVersionNo());
        planSchemeMapper.updatePlanScheme(scheme);
        return true;
    }

    @Override
    public List<Map<String, Object>> compareVersions(Long versionId1, Long versionId2) {
        PlanSchemeVersion v1 = versionMapper.selectPlanSchemeVersionById(versionId1);
        PlanSchemeVersion v2 = versionMapper.selectPlanSchemeVersionById(versionId2);
        List<Map<String, Object>> result = new ArrayList<>();

        if (v1 == null || v2 == null) return result;

        PlanScheme s1 = JSON.parseObject(v1.getSnapshotJson(), PlanScheme.class);
        PlanScheme s2 = JSON.parseObject(v2.getSnapshotJson(), PlanScheme.class);
        if (s1 == null || s2 == null) return result;

        // 比较字段差异
        java.lang.reflect.Field[] fields = PlanScheme.class.getDeclaredFields();
        for (java.lang.reflect.Field f : fields) {
            f.setAccessible(true);
            try {
                Object val1 = f.get(s1);
                Object val2 = f.get(s2);
                boolean diff = (val1 == null && val2 != null) || (val1 != null && !val1.equals(val2));
                if (diff) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("field", f.getName());
                    item.put("oldValue", val1 != null ? val1.toString() : "");
                    item.put("newValue", val2 != null ? val2.toString() : "");
                    result.add(item);
                }
            } catch (Exception ignored) {}
        }
        return result;
    }
}
