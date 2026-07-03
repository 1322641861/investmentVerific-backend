package com.ruoyi.modules.invest.message.mapper;

import com.ruoyi.modules.invest.message.domain.InvestMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvestMessageMapper {

    InvestMessage selectById(Long messageId);

    List<InvestMessage> selectList(InvestMessage msg);

    Long selectUnreadCount(@Param("receiverId") Long receiverId);

    int insert(InvestMessage msg);

    int markRead(@Param("messageId") Long messageId, @Param("readTime") java.util.Date readTime);

    int markAllRead(@Param("receiverId") Long receiverId, @Param("readTime") java.util.Date readTime);

    int deleteByIds(Long[] messageIds);
}
