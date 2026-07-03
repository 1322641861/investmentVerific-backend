package com.ruoyi.modules.invest.message.service;

import com.ruoyi.modules.invest.message.domain.InvestMessage;

import java.util.List;

public interface IInvestMessageService {

    List<InvestMessage> selectList(InvestMessage msg);

    Long selectUnreadCount(Long receiverId);

    int markRead(Long messageId);

    int markAllRead(Long receiverId);

    int sendMessage(InvestMessage msg);

    int sendBatch(List<Long> receiverIds, String title, String content, String messageType);

    int deleteByIds(Long[] messageIds);
}
