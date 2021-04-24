package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.service.FriendService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/friend")
@Slf4j
public class FriendController {

    @Resource
    private FriendService friendService;

    @RequestMapping("/getlist")
    public Result getFriendList() {
        return friendService.getFriendList();
    }

}
