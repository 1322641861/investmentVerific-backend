package com.ruoyi.modules.invest.contract.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.contract.domain.InvestContract;
import com.ruoyi.modules.invest.contract.mapper.InvestContractMapper;
import com.ruoyi.modules.invest.contract.service.IInvestContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投资合同Service业务层
 *
 * @author investvf
 */
@Service
public class InvestContractServiceImpl implements IInvestContractService {

    @Autowired
    private InvestContractMapper investContractMapper;

    @Override
    public InvestContract selectInvestContractById(Long contractId) {
        return investContractMapper.selectInvestContractById(contractId);
    }

    @Override
    public List<InvestContract> selectInvestContractList(InvestContract contract) {
        return investContractMapper.selectInvestContractList(contract);
    }

    @Override
    public int insertInvestContract(InvestContract contract) {
        contract.setCreateBy(ShiroUtils.getLoginName());
        contract.setCreateTime(DateUtils.getNowDate());
        contract.setDelFlag("0");
        if (contract.getContractStatus() == null) {
            contract.setContractStatus("0");
        }
        return investContractMapper.insertInvestContract(contract);
    }

    @Override
    public int updateInvestContract(InvestContract contract) {
        contract.setUpdateBy(ShiroUtils.getLoginName());
        contract.setUpdateTime(DateUtils.getNowDate());
        return investContractMapper.updateInvestContract(contract);
    }

    @Override
    public int deleteInvestContractByIds(Long[] ids) {
        return investContractMapper.deleteInvestContractByIds(ids);
    }

    @Override
    public List<InvestContract> selectByProjectId(Long projectId) {
        return investContractMapper.selectByProjectId(projectId);
    }
}
