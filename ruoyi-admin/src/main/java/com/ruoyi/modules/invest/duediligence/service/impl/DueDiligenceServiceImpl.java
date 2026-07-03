package com.ruoyi.modules.invest.duediligence.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.duediligence.domain.DueDiligence;
import com.ruoyi.modules.invest.duediligence.mapper.DueDiligenceMapper;
import com.ruoyi.modules.invest.duediligence.service.IDueDiligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 尽职调查Service业务层
 *
 * @author investvf
 */
@Service
public class DueDiligenceServiceImpl implements IDueDiligenceService {

    @Autowired
    private DueDiligenceMapper dueDiligenceMapper;

    @Override
    public DueDiligence selectDueDiligenceById(Long dueDiligenceId) {
        return dueDiligenceMapper.selectDueDiligenceById(dueDiligenceId);
    }

    @Override
    public List<DueDiligence> selectDueDiligenceList(DueDiligence dueDiligence) {
        return dueDiligenceMapper.selectDueDiligenceList(dueDiligence);
    }

    @Override
    public int insertDueDiligence(DueDiligence dueDiligence) {
        dueDiligence.setCreateBy(ShiroUtils.getLoginName());
        dueDiligence.setCreateTime(DateUtils.getNowDate());
        dueDiligence.setDelFlag("0");
        if (dueDiligence.getDdStatus() == null) {
            dueDiligence.setDdStatus("0");
        }
        return dueDiligenceMapper.insertDueDiligence(dueDiligence);
    }

    @Override
    public int updateDueDiligence(DueDiligence dueDiligence) {
        dueDiligence.setUpdateBy(ShiroUtils.getLoginName());
        dueDiligence.setUpdateTime(DateUtils.getNowDate());
        return dueDiligenceMapper.updateDueDiligence(dueDiligence);
    }

    @Override
    public int deleteDueDiligenceByIds(Long[] ids) {
        return dueDiligenceMapper.deleteDueDiligenceByIds(ids);
    }

    @Override
    public DueDiligence selectByProjectId(Long projectId) {
        return dueDiligenceMapper.selectByProjectId(projectId);
    }
}
