package com.ruoyi.modules.invest.execution.milestone.service;

import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import java.util.List;

public interface IInvestProjectMilestoneService { InvestProjectMilestone selectById(Long id); List<InvestProjectMilestone> selectList(InvestProjectMilestone m); List<InvestProjectMilestone> selectByProjectId(Long projectId); int insert(InvestProjectMilestone m); int update(InvestProjectMilestone m); int deleteByIds(Long[] ids); boolean complete(Long id); boolean accept(Long id, String opinion); boolean rejectAcceptance(Long id, String opinion); }
