package com.scu.lcw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.lcw.blog.entity.MessageDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:21
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageDO> {
}
