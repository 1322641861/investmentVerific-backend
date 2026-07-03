package com.ruoyi.modules.invest.execution.milestone.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.execution.milestone.domain.InvestProjectMilestone;
import com.ruoyi.modules.invest.execution.milestone.mapper.InvestProjectMilestoneMapper;
import com.ruoyi.modules.invest.execution.milestone.service.IInvestProjectMilestoneService;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvestProjectMilestoneServiceImpl implements IInvestProjectMilestoneService {
    @Autowired private InvestProjectMilestoneMapper mapper; @Autowired private InvestProjectMapper projectMapper;
    public InvestProjectMilestone selectById(Long id){return mapper.selectById(id);} public List<InvestProjectMilestone> selectList(InvestProjectMilestone m){return mapper.selectList(m);} public List<InvestProjectMilestone> selectByProjectId(Long projectId){return mapper.selectByProjectId(projectId);}
    public int insert(InvestProjectMilestone m){fillProject(m);m.setCreateBy(ShiroUtils.getLoginName());m.setCreateTime(DateUtils.getNowDate());m.setDelFlag("0");if(m.getStatus()==null)m.setStatus("0");if(m.getMilestoneCode()==null||m.getMilestoneCode().isEmpty())m.setMilestoneCode("MS-"+System.currentTimeMillis());return mapper.insert(m);} public int update(InvestProjectMilestone m){m.setUpdateBy(ShiroUtils.getLoginName());m.setUpdateTime(DateUtils.getNowDate());return mapper.update(m);} public int deleteByIds(Long[] ids){return mapper.deleteByIds(ids);}
    @Transactional(rollbackFor=Exception.class) public boolean complete(Long id){InvestProjectMilestone m=mapper.selectById(id);if(m==null)return false;m.setStatus("2");m.setProgressRate(new BigDecimal("100"));m.setActualEndDate(DateUtils.getNowDate());m.setUpdateBy(ShiroUtils.getLoginName());m.setUpdateTime(DateUtils.getNowDate());mapper.update(m);recalcProjectProgress(m.getProjectId());return true;}

    @Transactional(rollbackFor=Exception.class) public boolean accept(Long id, String opinion) {
        InvestProjectMilestone m = mapper.selectById(id);
        if (m == null || !"2".equals(m.getStatus())) return false;
        m.setAcceptanceStatus("1");
        m.setAcceptanceBy(ShiroUtils.getLoginName());
        m.setAcceptanceTime(DateUtils.getNowDate());
        m.setAcceptanceOpinion(opinion);
        m.setUpdateBy(ShiroUtils.getLoginName());
        m.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(m) > 0;
    }

    @Transactional(rollbackFor=Exception.class) public boolean rejectAcceptance(Long id, String opinion) {
        InvestProjectMilestone m = mapper.selectById(id);
        if (m == null || !"2".equals(m.getStatus())) return false;
        m.setAcceptanceStatus("2");
        m.setAcceptanceBy(ShiroUtils.getLoginName());
        m.setAcceptanceTime(DateUtils.getNowDate());
        m.setAcceptanceOpinion(opinion);
        m.setUpdateBy(ShiroUtils.getLoginName());
        m.setUpdateTime(DateUtils.getNowDate());
        return mapper.update(m) > 0;
    }

    public void recalcProjectProgress(Long projectId){List<InvestProjectMilestone> list=mapper.selectByProjectId(projectId);BigDecimal total=BigDecimal.ZERO, score=BigDecimal.ZERO;for(InvestProjectMilestone m:list){BigDecimal w=m.getWeight()==null?BigDecimal.ZERO:m.getWeight();BigDecimal p=m.getProgressRate()==null?BigDecimal.ZERO:m.getProgressRate();total=total.add(w);score=score.add(w.multiply(p));}BigDecimal progress=total.compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:score.divide(total,2,RoundingMode.HALF_UP);InvestProject p=new InvestProject();p.setProjectId(projectId);p.setProgressRate(progress);p.setUpdateBy(ShiroUtils.getLoginName());p.setUpdateTime(DateUtils.getNowDate());projectMapper.updateInvestProject(p);}
    private void fillProject(InvestProjectMilestone m){if(m.getProjectId()==null)return;InvestProject p=projectMapper.selectInvestProjectById(m.getProjectId());if(p!=null){m.setProjectCode(p.getProjectCode());m.setProjectName(p.getProjectName());m.setSchemeId(p.getSchemeId());m.setPlanYear(p.getPlanYear());}}
}
