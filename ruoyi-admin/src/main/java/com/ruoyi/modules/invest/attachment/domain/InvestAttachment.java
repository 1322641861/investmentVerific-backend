package com.ruoyi.modules.invest.attachment.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvestAttachment extends BaseEntity {
    private Long attachmentId;
    private String businessType;
    private Long businessId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String fileExt;
    private String uploadBy;
    private java.util.Date uploadTime;
    private String delFlag;
}
