package com.ruoyi.modules.invest.execution.warning.service;

import com.ruoyi.modules.invest.execution.warning.domain.InvestProgressWarning;
import java.util.List;

public interface IInvestProgressWarningService {
    List<InvestProgressWarning> selectList(InvestProgressWarning w);
    int scan(Long projectId);
    int scanAllProjects();
    boolean handle(Long id, String status, String opinion);
}
