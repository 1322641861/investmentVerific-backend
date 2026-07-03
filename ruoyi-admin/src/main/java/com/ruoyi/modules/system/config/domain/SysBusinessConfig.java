package com.ruoyi.modules.system.config.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysBusinessConfig extends BaseEntity {
    private Long configId;
    private String configCode;
    private String configName;
    private String configValue;
    private String configType;
    private String configOptions;
    private String status;
}
