package com.ruoyi.modules.invest.attachment.mapper;

import com.ruoyi.modules.invest.attachment.domain.InvestAttachment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InvestAttachmentMapper {
    InvestAttachment selectById(Long id);
    List<InvestAttachment> selectList(InvestAttachment att);
    List<InvestAttachment> selectByBusiness(String businessType, Long businessId);
    int insert(InvestAttachment att);
    int deleteByIds(Long[] ids);
    int deleteByBusiness(String businessType, Long businessId);
}
