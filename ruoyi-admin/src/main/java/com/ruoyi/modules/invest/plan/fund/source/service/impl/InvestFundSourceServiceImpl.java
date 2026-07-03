package com.ruoyi.modules.invest.plan.fund.source.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.invest.plan.fund.source.domain.InvestFundSource;
import com.ruoyi.modules.invest.plan.fund.source.mapper.InvestFundSourceMapper;
import com.ruoyi.modules.invest.plan.fund.source.service.IInvestFundSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestFundSourceServiceImpl implements IInvestFundSourceService {
    @Autowired
    private InvestFundSourceMapper sourceMapper;

    public InvestFundSource selectInvestFundSourceById(Long sourceId) { return sourceMapper.selectInvestFundSourceById(sourceId); }
    public List<InvestFundSource> selectInvestFundSourceList(InvestFundSource source) { return sourceMapper.selectInvestFundSourceList(source); }
    public int insertInvestFundSource(InvestFundSource source) { source.setCreateBy(ShiroUtils.getLoginName()); source.setCreateTime(DateUtils.getNowDate()); source.setDelFlag("0"); if(source.getStatus()==null){source.setStatus("0");} return sourceMapper.insertInvestFundSource(source); }
    public int updateInvestFundSource(InvestFundSource source) { source.setUpdateBy(ShiroUtils.getLoginName()); source.setUpdateTime(DateUtils.getNowDate()); return sourceMapper.updateInvestFundSource(source); }
    public int deleteInvestFundSourceByIds(Long[] sourceIds) { return sourceMapper.deleteInvestFundSourceByIds(sourceIds); }
}
