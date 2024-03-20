package com.liusheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liusheng.model.Message;
import com.liusheng.service.MessageService;
import com.liusheng.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 35793
* @description 针对表【message(消息表，用于广播消息)】的数据库操作Service实现
* @createDate 2024-03-20 11:55:03
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




