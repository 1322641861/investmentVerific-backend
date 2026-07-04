package com.ruoyi.modules.invest.contract.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资合同表
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestContract extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    private Long contractId;

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类型（1投资协议 2股东协议 3其他）
     */
    private String contractType;

    /**
     * 投资金额（万元）
     */
    private BigDecimal investAmount;

    /**
     * 持股比例（%）
     */
    private BigDecimal shareRatio;

    /**
     * 投资方
     */
    private String investor;

    /**
     * 融资方
     */
    private String funder;

    /**
     * 签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 合同状态（0起草 1审批中 2已生效 3已终止 4已解除）
     */
    private String contractStatus;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 合同文件地址（PDF）
     */
    private String filePath;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志
     */
    private String delFlag;
}
