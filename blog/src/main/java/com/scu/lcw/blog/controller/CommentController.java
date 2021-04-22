package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @RequestMapping("/daily")
    public Result addDailyParentComment(CommentRequest commentRequest, HttpServletRequest request) {
        return commentService.addDailyComment(commentRequest, request);
    }
}
