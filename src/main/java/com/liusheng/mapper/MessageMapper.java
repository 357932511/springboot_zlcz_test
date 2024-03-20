package com.liusheng.mapper;

import com.liusheng.model.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 35793
* @description 针对表【message(消息表，用于广播消息)】的数据库操作Mapper
* @createDate 2024-03-20 11:55:03
* @Entity com.liusheng.model.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 获得三小时以上未读信息
     * @return 三小时以上未读信息
     */
    List<Message> getUnreadMessage();
}




