package com.ruoyi.modules.invest.execution.report.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import com.ruoyi.modules.invest.execution.milestone.service.IInvestProjectMilestoneService;
import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReport;
import com.ruoyi.modules.invest.execution.report.domain.InvestProgressReviewLog;
import com.ruoyi.modules.invest.execution.report.domain.dto.ProgressReviewRequest;
import com.ruoyi.modules.invest.execution.report.mapper.InvestProgressReportMapper;
import com.ruoyi.modules.invest.execution.report.mapper.InvestProgressReviewLogMapper;
import com.ruoyi.modules.invest.execution.report.service.IInvestProgressReportService;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InvestProgressReportServiceImpl implements IInvestProgressReportService {
 @Autowired private InvestProgressReportMapper mapper; @Autowired private InvestProgressReviewLogMapper logMapper; @Autowired private InvestProjectMapper projectMapper; @Autowired private IInvestProjectMilestoneService milestoneService;
 public InvestProgressReport selectById(Long id){return mapper.selectById(id);} public List<InvestProgressReport> selectList(InvestProgressReport r){return mapper.selectList(r);} public int insert(InvestProgressReport r){fillProject(r);r.setCreateBy(ShiroUtils.getLoginName());r.setCreateTime(DateUtils.getNowDate());r.setDelFlag("0");if(r.getReportStatus()==null)r.setReportStatus("0");if(r.getReportCode()==null||r.getReportCode().isEmpty())r.setReportCode("RPT-"+System.currentTimeMillis());return mapper.insert(r);} public int update(InvestProgressReport r){r.setUpdateBy(ShiroUtils.getLoginName());r.setUpdateTime(DateUtils.getNowDate());return mapper.update(r);} public int deleteByIds(Long[] ids){return mapper.deleteByIds(ids);}
 @Transactional(rollbackFor=Exception.class) public boolean submit(Long id){InvestProgressReport r=mapper.selectById(id);if(r==null)return false;r.setReportStatus("1");r.setReportTime(DateUtils.getNowDate());r.setReporterId(ShiroUtils.getUserId());r.setReporterName(ShiroUtils.getSysUser().getUserName());mapper.update(r);log(id,"1",null);return true;}
 public boolean withdraw(Long id){InvestProgressReport r=mapper.selectById(id);if(r==null||!"1".equals(r.getReportStatus()))return false;r.setReportStatus("4");mapper.update(r);log(id,"4",null);return true;}
 @Transactional(rollbackFor=Exception.class) public boolean review(Long id, ProgressReviewRequest req){InvestProgressReport r=mapper.selectById(id);if(r==null||!"1".equals(r.getReportStatus()))return false;String action=req.getAction();r.setReportStatus("2".equals(action)?"2":"3");r.setReviewerId(ShiroUtils.getUserId());r.setReviewerName(ShiroUtils.getSysUser().getUserName());r.setReviewTime(DateUtils.getNowDate());r.setReviewOpinion(req.getOpinion());mapper.update(r);log(id,action,req.getOpinion());if("2".equals(action)&&r.getMilestoneId()!=null){InvestProjectMilestone m=milestoneService.selectById(r.getMilestoneId());if(m!=null){m.setProgressRate(r.getProgressRate());if(r.getProgressRate()!=null&&r.getProgressRate().intValue()>=100)m.setStatus("2");milestoneService.update(m);}}return true;}
 private void log(Long id,String action,String opinion){InvestProgressReviewLog l=new InvestProgressReviewLog();l.setReportId(id);l.setAction(action);l.setOperatorId(ShiroUtils.getUserId());l.setOperatorName(ShiroUtils.getSysUser().getUserName());l.setOpinion(opinion);l.setCreateTime(DateUtils.getNowDate());logMapper.insert(l);} private void fillProject(InvestProgressReport r){if(r.getProjectId()==null)return;InvestProject p=projectMapper.selectInvestProjectById(r.getProjectId());if(p!=null){r.setProjectName(p.getProjectName());if(r.getPlanAmount()==null)r.setPlanAmount(p.getProposedAmount());}}
}
