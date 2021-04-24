package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.pojo.request.CommentLikeRequest;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.blog.pojo.request.LoginMessageRequest;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.util.AntiBrushUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Resource
    private CommentService commentService;

    @Resource
    private AntiBrushUtils antiBrushUtils;

    @RequestMapping("/daily")
    public Result addDailyParentComment(CommentRequest commentRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(commentRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(commentRequest.getBlogUserLoginFlag())) {
            return commentService.addDailyComment(commentRequest);
        }
        return Result.ok();
    }

    @RequestMapping("/likecomment")
    public Result likeComment(CommentLikeRequest commentLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(commentLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(commentLikeRequest.getBlogUserLoginFlag())) {
            Result result = commentService.likeComment(CommentDO.buildDailyDOByLikeRequest(commentLikeRequest), commentLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(commentLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/dislikecomment")
    public Result dislikeComment(CommentLikeRequest commentLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(commentLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(commentLikeRequest.getBlogUserLoginFlag())) {
            Result result = commentService.dislikeComment(CommentDO.buildDailyDOByLikeRequest(commentLikeRequest), commentLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(commentLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/likecommentlist")
    public Result getLikeCommentList(LoginMessageRequest loginMessageRequest) {
        return commentService.getLikeCommentList(loginMessageRequest.getBlogUserLoginFlag());
    }

    @RequestMapping("/dislikecommentlist")
    public Result getDislikeCommentList(LoginMessageRequest loginMessageRequest) {
        return commentService.getDislikeCommentList(loginMessageRequest.getBlogUserLoginFlag());
    }
}
