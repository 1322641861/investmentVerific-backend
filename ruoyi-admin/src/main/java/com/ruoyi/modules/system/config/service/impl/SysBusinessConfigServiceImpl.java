package com.ruoyi.modules.system.config.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.modules.system.config.domain.SysBusinessConfig;
import com.ruoyi.modules.system.config.mapper.SysBusinessConfigMapper;
import com.ruoyi.modules.system.config.service.ISysBusinessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysBusinessConfigServiceImpl implements ISysBusinessConfigService {
    @Autowired private SysBusinessConfigMapper mapper;
    public List<SysBusinessConfig> selectList(SysBusinessConfig config) { return mapper.selectList(config); }
    public SysBusinessConfig selectById(Long configId) { return mapper.selectById(configId); }
    public SysBusinessConfig selectByCode(String configCode) { return mapper.selectByCode(configCode); }
    public int insert(SysBusinessConfig config) { config.setCreateBy(ShiroUtils.getLoginName()); config.setCreateTime(DateUtils.getNowDate()); return mapper.insert(config); }
    public int update(SysBusinessConfig config) { config.setUpdateBy(ShiroUtils.getLoginName()); config.setUpdateTime(DateUtils.getNowDate()); return mapper.update(config); }
    public int deleteByIds(Long[] configIds) { return mapper.deleteByIds(configIds); }
}
