package com.ruoyi.modules.invest.attachment.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.modules.invest.attachment.domain.InvestAttachment;
import com.ruoyi.modules.invest.attachment.mapper.InvestAttachmentMapper;
import com.ruoyi.modules.invest.attachment.service.IInvestAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class InvestAttachmentServiceImpl implements IInvestAttachmentService {
    @Autowired private InvestAttachmentMapper mapper;

    public InvestAttachment selectById(Long id) { return mapper.selectById(id); }
    public List<InvestAttachment> selectList(InvestAttachment att) { return mapper.selectList(att); }
    public List<InvestAttachment> selectByBusiness(String businessType, Long businessId) { return mapper.selectByBusiness(businessType, businessId); }

    @Override
    public InvestAttachment uploadFile(MultipartFile file, String businessType, Long businessId) {
        try {
            String filePath = FileUploadUtils.upload(file);
            String fileName = file.getOriginalFilename();

            InvestAttachment att = new InvestAttachment();
            att.setBusinessType(businessType);
            att.setBusinessId(businessId);
            att.setFileName(fileName);
            att.setFilePath(filePath);
            att.setFileSize(file.getSize());
            att.setFileType(file.getContentType());
            att.setFileExt(fileName != null && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")+1) : "");
            att.setUploadBy(ShiroUtils.getLoginName());
            att.setUploadTime(DateUtils.getNowDate());
            att.setCreateBy(att.getUploadBy());
            att.setCreateTime(att.getUploadTime());
            mapper.insert(att);
            return att;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    public int deleteByIds(Long[] ids) { return mapper.deleteByIds(ids); }
    public int deleteByBusiness(String businessType, Long businessId) { return mapper.deleteByBusiness(businessType, businessId); }
}
