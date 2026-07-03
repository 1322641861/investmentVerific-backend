package com.ruoyi.modules.invest.contract.mapper;

import com.ruoyi.modules.invest.contract.domain.InvestContract;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投资合同Mapper接口
 *
 * @author investvf
 */
@Mapper
public interface InvestContractMapper {

    InvestContract selectInvestContractById(Long contractId);

    List<InvestContract> selectInvestContractList(InvestContract contract);

    int insertInvestContract(InvestContract contract);

    int updateInvestContract(InvestContract contract);

    int deleteInvestContractByIds(Long[] ids);

    List<InvestContract> selectByProjectId(Long projectId);
}
