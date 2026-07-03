package com.ruoyi.modules.invest.attachment.service;

import com.ruoyi.modules.invest.attachment.domain.InvestAttachment;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IInvestAttachmentService {
    InvestAttachment selectById(Long id);
    List<InvestAttachment> selectList(InvestAttachment att);
    List<InvestAttachment> selectByBusiness(String businessType, Long businessId);
    InvestAttachment uploadFile(MultipartFile file, String businessType, Long businessId);
    int deleteByIds(Long[] ids);
    int deleteByBusiness(String businessType, Long businessId);
}
