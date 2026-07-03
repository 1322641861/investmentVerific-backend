package com.ruoyi.modules.invest.contract.service;

import com.ruoyi.modules.invest.contract.domain.InvestContract;

import java.util.List;

/**
 * 投资合同Service接口
 *
 * @author investvf
 */
public interface IInvestContractService {

    InvestContract selectInvestContractById(Long contractId);

    List<InvestContract> selectInvestContractList(InvestContract contract);

    int insertInvestContract(InvestContract contract);

    int updateInvestContract(InvestContract contract);

    int deleteInvestContractByIds(Long[] ids);

    List<InvestContract> selectByProjectId(Long projectId);
}
