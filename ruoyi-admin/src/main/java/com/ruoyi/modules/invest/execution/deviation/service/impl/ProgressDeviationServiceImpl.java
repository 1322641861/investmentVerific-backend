package com.ruoyi.modules.invest.execution.deviation.service.impl;

import com.ruoyi.modules.invest.execution.deviation.domain.dto.ProgressDeviationVO;
import com.ruoyi.modules.invest.execution.deviation.service.IProgressDeviationService;
import com.ruoyi.modules.invest.project.domain.InvestProject;
import com.ruoyi.modules.invest.project.mapper.InvestProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ProgressDeviationServiceImpl implements IProgressDeviationService {
 @Autowired private InvestProjectMapper projectMapper;
 public ProgressDeviationVO getProjectDeviation(Long projectId){InvestProject p=projectMapper.selectInvestProjectById(projectId);ProgressDeviationVO vo=new ProgressDeviationVO();vo.setProjectId(projectId);BigDecimal actual=p==null||p.getProgressRate()==null?BigDecimal.ZERO:p.getProgressRate();vo.setActualProgress(actual);vo.setExpectedProgress(new BigDecimal("50"));vo.setProgressDeviation(actual.subtract(vo.getExpectedProgress()));vo.setPaymentCoverage(BigDecimal.ZERO);vo.setSummary("当前进度偏差："+vo.getProgressDeviation()+"%");return vo;}
}
