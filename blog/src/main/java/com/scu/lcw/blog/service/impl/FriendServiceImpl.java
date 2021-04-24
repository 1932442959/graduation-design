package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.mapper.FriendMapper;
import com.scu.lcw.blog.service.FriendService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendMapper friendMapper;

    @Override
    public Result getFriendList() {
        return Result.data(friendMapper.selectList(new QueryWrapper<>()));
    }
}
