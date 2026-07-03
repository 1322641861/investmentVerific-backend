package com.ruoyi.modules.system.config.service;

import com.ruoyi.modules.system.config.domain.SysBusinessConfig;
import java.util.List;

public interface ISysBusinessConfigService {
    List<SysBusinessConfig> selectList(SysBusinessConfig config);
    SysBusinessConfig selectById(Long configId);
    SysBusinessConfig selectByCode(String configCode);
    int insert(SysBusinessConfig config);
    int update(SysBusinessConfig config);
    int deleteByIds(Long[] configIds);
}
