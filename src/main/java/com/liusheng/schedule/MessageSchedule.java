package com.liusheng.schedule;

import com.liusheng.mapper.MessageMapper;
import com.liusheng.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liusheng
 * @date 2024/3/2011:37
 * @desc 消息相关事务
 */
@Component
public class MessageSchedule {

    private final MessageMapper messageMapper;

    public MessageSchedule(@Autowired MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    // 三小时检测一次数据库，是否有3小时以上的未读信息
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void messageIsRead(){
        // 获得三小时以上未读信息
        List<Message> messages = messageMapper.getUnreadMessage();
        for (Message message : messages) {
            // TODO 调用第三方接口发送信息
        }
    }
}
