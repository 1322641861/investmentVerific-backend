package com.ruoyi.modules.system.config.mapper;

import com.ruoyi.modules.system.config.domain.SysBusinessConfig;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysBusinessConfigMapper {
    SysBusinessConfig selectById(Long configId);
    SysBusinessConfig selectByCode(String configCode);
    List<SysBusinessConfig> selectList(SysBusinessConfig config);
    int insert(SysBusinessConfig config);
    int update(SysBusinessConfig config);
    int deleteByIds(Long[] configIds);
}
