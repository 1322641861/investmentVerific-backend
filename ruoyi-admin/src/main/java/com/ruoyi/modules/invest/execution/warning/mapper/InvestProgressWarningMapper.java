package com.ruoyi.modules.invest.execution.warning.mapper;

import com.ruoyi.modules.invest.execution.warning.domain.InvestProgressWarning;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestProgressWarningMapper { InvestProgressWarning selectById(Long warningId); List<InvestProgressWarning> selectList(InvestProgressWarning w); int insert(InvestProgressWarning w); int update(InvestProgressWarning w); }
