package com.ruoyi.modules.invest.execution.milestone.mapper;

import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestProjectMilestoneMapper { InvestProjectMilestone selectById(Long milestoneId); List<InvestProjectMilestone> selectList(InvestProjectMilestone m); int insert(InvestProjectMilestone m); int update(InvestProjectMilestone m); int deleteByIds(Long[] ids); List<InvestProjectMilestone> selectByProjectId(Long projectId); }
