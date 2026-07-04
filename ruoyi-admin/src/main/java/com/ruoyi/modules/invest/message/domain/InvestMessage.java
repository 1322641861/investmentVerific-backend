package com.ruoyi.modules.invest.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 消息通知表 invest_message
 *
 * @author investvf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestMessage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long messageId;
    private String messageCode;
    private String messageType;
    private String title;
    private String content;
    private Long receiverId;
    private String receiverName;
    private Long senderId;
    private String senderName;
    private String businessType;
    private Long businessId;
    private String businessUrl;
    private String isRead;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date readTime;
    private String delFlag;
}
