package com.scu.lcw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.lcw.blog.entity.CommentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 14:51
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {
}
