package com.ruoyi.modules.invest.duediligence.mapper;

import com.ruoyi.modules.invest.duediligence.domain.DueDiligence;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 尽职调查Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface DueDiligenceMapper {

    DueDiligence selectDueDiligenceById(Long dueDiligenceId);

    List<DueDiligence> selectDueDiligenceList(DueDiligence dueDiligence);

    int insertDueDiligence(DueDiligence dueDiligence);

    int updateDueDiligence(DueDiligence dueDiligence);

    int deleteDueDiligenceByIds(Long[] ids);

    DueDiligence selectByProjectId(Long projectId);
}
