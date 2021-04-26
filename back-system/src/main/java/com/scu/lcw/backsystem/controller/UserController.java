package com.scu.lcw.backsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.backsystem.entity.BlogUserDO;
import com.scu.lcw.backsystem.mapper.BlogUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserController {

    @Resource
    private BlogUserMapper blogUserMapper;

    public BlogUserDO findManager() {
        return blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", 2L)).get(0);
    }
}
