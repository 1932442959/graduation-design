package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.FriendRequest;
import com.scu.lcw.common.response.Result;

public interface FriendService {

    Result getFriendList();

    Result addApply(FriendRequest friendRequest);

    Result findApply();

    Result save(FriendRequest friendRequest);

    Result delete(FriendRequest friendRequest);

}
