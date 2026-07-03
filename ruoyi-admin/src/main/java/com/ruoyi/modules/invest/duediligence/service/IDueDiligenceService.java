package com.ruoyi.modules.invest.duediligence.service;

import com.ruoyi.modules.invest.duediligence.domain.DueDiligence;

import java.util.List;

/**
 * 尽职调查Service接口
 *
 * @author investvf
 */
public interface IDueDiligenceService {

    DueDiligence selectDueDiligenceById(Long dueDiligenceId);

    List<DueDiligence> selectDueDiligenceList(DueDiligence dueDiligence);

    int insertDueDiligence(DueDiligence dueDiligence);

    int updateDueDiligence(DueDiligence dueDiligence);

    int deleteDueDiligenceByIds(Long[] ids);

    DueDiligence selectByProjectId(Long projectId);
}
