package com.ruoyi.modules.invest.plan.fund.plan.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金配套率趋势VO
 *
 * @author investvf
 */
@Data
public class FundMatchingTrendVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    private BigDecimal matchRate = BigDecimal.ZERO;
    private BigDecimal arrivedRate = BigDecimal.ZERO;
    private BigDecimal paidRate = BigDecimal.ZERO;
}
