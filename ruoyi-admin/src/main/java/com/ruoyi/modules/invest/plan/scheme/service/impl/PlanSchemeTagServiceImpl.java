package com.ruoyi.modules.invest.plan.scheme.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.scheme.domain.PlanSchemeTag;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeTagMapper;
import com.ruoyi.modules.invest.plan.scheme.mapper.PlanSchemeTagRelMapper;
import com.ruoyi.modules.invest.plan.scheme.service.IPlanSchemeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 规划方案标签Service业务层处理
 *
 * @author investvf
 */
@Service
public class PlanSchemeTagServiceImpl implements IPlanSchemeTagService {

    @Autowired
    private PlanSchemeTagMapper tagMapper;

    @Autowired
    private PlanSchemeTagRelMapper tagRelMapper;

    @Override
    public PlanSchemeTag selectPlanSchemeTagById(Long tagId) {
        return tagMapper.selectPlanSchemeTagById(tagId);
    }

    @Override
    public List<PlanSchemeTag> selectPlanSchemeTagList(PlanSchemeTag tag) {
        return tagMapper.selectPlanSchemeTagList(tag);
    }

    @Override
    public List<PlanSchemeTag> selectTagsBySchemeId(Long schemeId) {
        return tagMapper.selectTagsBySchemeId(schemeId);
    }

    @Override
    public int insertPlanSchemeTag(PlanSchemeTag tag) {
        tag.setCreateBy(ShiroUtils.getLoginName());
        tag.setCreateTime(DateUtils.getNowDate());
        if (tag.getStatus() == null) {
            tag.setStatus("0");
        }
        if (tag.getOrderNum() == null) {
            tag.setOrderNum(0);
        }
        return tagMapper.insertPlanSchemeTag(tag);
    }

    @Override
    public int updatePlanSchemeTag(PlanSchemeTag tag) {
        tag.setUpdateBy(ShiroUtils.getLoginName());
        tag.setUpdateTime(DateUtils.getNowDate());
        return tagMapper.updatePlanSchemeTag(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePlanSchemeTagByIds(Long[] tagIds) {
        tagRelMapper.deleteByTagIds(tagIds);
        return tagMapper.deletePlanSchemeTagByIds(tagIds);
    }
}
