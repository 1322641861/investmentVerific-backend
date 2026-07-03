package com.ruoyi.modules.invest.plan.scheme.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规划方案版本快照表 plan_scheme_version
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanSchemeVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 版本ID */
    private Long versionId;

    /** 方案ID */
    private Long schemeId;

    /** 版本号 */
    private Integer versionNo;

    /** 变更说明 */
    private String changeLog;

    /** 方案快照JSON */
    private String snapshotJson;

    /** 是否当前版本（0否 1是） */
    private String isCurrent;
}
