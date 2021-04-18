package com.scu.lcw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.lcw.blog.entity.BlogUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogUserMapper extends BaseMapper<BlogUserDO> {
}
