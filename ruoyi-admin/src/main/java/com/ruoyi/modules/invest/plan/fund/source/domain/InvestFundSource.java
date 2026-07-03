package com.ruoyi.modules.invest.plan.fund.source.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 资金来源主档 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestFundSource extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long sourceId;
    private String sourceCode;
    private String sourceName;
    private String sourceType;
    private String subjectName;
    private String bankName;
    private String bankAccount;
    private String status;
    private String delFlag;
}
