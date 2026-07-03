package com.ruoyi.modules.invest.message.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.modules.invest.message.domain.InvestMessage;
import com.ruoyi.modules.invest.message.mapper.InvestMessageMapper;
import com.ruoyi.modules.invest.message.service.IInvestMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvestMessageServiceImpl implements IInvestMessageService {

    @Autowired
    private InvestMessageMapper mapper;

    @Override
    public List<InvestMessage> selectList(InvestMessage msg) {
        return mapper.selectList(msg);
    }

    @Override
    public Long selectUnreadCount(Long receiverId) {
        return mapper.selectUnreadCount(receiverId);
    }

    @Override
    public int markRead(Long messageId) {
        return mapper.markRead(messageId, DateUtils.getNowDate());
    }

    @Override
    public int markAllRead(Long receiverId) {
        return mapper.markAllRead(receiverId, DateUtils.getNowDate());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendMessage(InvestMessage msg) {
        if (msg.getMessageCode() == null) {
            msg.setMessageCode("MSG-" + System.currentTimeMillis());
        }
        msg.setIsRead("0");
        msg.setCreateTime(DateUtils.getNowDate());
        return mapper.insert(msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendBatch(List<Long> receiverIds, String title, String content, String messageType) {
        int rows = 0;
        for (Long receiverId : receiverIds) {
            InvestMessage msg = new InvestMessage();
            msg.setMessageCode("MSG-" + System.currentTimeMillis() + "-" + receiverId);
            msg.setMessageType(messageType);
            msg.setTitle(title);
            msg.setContent(content);
            msg.setReceiverId(receiverId);
            msg.setCreateTime(DateUtils.getNowDate());
            rows += mapper.insert(msg);
        }
        return rows;
    }

    @Override
    public int deleteByIds(Long[] messageIds) {
        return mapper.deleteByIds(messageIds);
    }
}
