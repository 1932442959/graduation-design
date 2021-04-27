package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.FriendDO;
import com.scu.lcw.blog.mapper.FriendMapper;
import com.scu.lcw.blog.pojo.request.FriendRequest;
import com.scu.lcw.blog.service.FriendService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FriendServiceImpl extends BaseController implements FriendService {

    @Resource
    private FriendMapper friendMapper;

    @Override
    public Result getFriendList() {
        return Result.data(friendMapper.selectList(new QueryWrapper<FriendDO>().eq("need_operate", 0)));
    }

    @Override
    public Result addApply(FriendRequest friendRequest) {
        BlogUserDO user = this.getBlogUserMessage(friendRequest.getBlogUserLoginFlag());
        return Result.data(friendMapper.insert(new FriendDO()
                .setFriendUrl(friendRequest.getInputUrl())
                .setFriendIntro(friendRequest.getInputIntro())
                .setFriendAvator(user.getUserAvator())
                .setFriendNetname(user.getUserNetname())
                .setFriendUsername(user.getUserName())
                .setNeedOperate("1")));
    }

    @Override
    public Result findApply() {
        return Result.data(friendMapper.selectList(new QueryWrapper<FriendDO>().eq("need_operate", 1)));
    }

    @Override
    public Result save(FriendRequest friendRequest) {
        FriendDO friend = friendMapper.selectList(new QueryWrapper<FriendDO>().eq("friend_id", friendRequest.getFriendId())).get(0);
        return Result.data(friendMapper.update(friend.setNeedOperate("0"), new QueryWrapper<FriendDO>().eq("friend_id", friend.getFriendId())));
    }

    @Override
    public Result delete(FriendRequest friendRequest) {
        return Result.data(friendMapper.delete(new QueryWrapper<FriendDO>().eq("friend_id", friendRequest.getFriendId())));
    }
}
