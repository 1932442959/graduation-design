package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.entity.CommentLikeRequest;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.util.AntiBrushUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Resource
    private CommentService commentService;

    @Resource
    private AntiBrushUtils antiBrushUtils;

    @RequestMapping("/daily")
    public Result addDailyParentComment(CommentRequest commentRequest, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(request)) {
            return commentService.addDailyComment(commentRequest, request);
        }
        return Result.ok();
    }

    @RequestMapping("/likecomment")
    public Result likeComment(CommentLikeRequest commentLikeRequest, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(request)) {
            Result result = commentService.likeComment(CommentDO.buildDailyDOByLikeRequest(commentLikeRequest), request);
            this.updateBlogUserMessage(request);
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/dislikecomment")
    public Result dislikeComment(CommentLikeRequest commentLikeRequest, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(request)) {
            Result result = commentService.dislikeComment(CommentDO.buildDailyDOByLikeRequest(commentLikeRequest), request);
            this.updateBlogUserMessage(request);
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/likecommentlist")
    public Result getLikeCommentList(HttpServletRequest request) {
        return commentService.getLikeCommentList(request);
    }

    @RequestMapping("/dislikecommentlist")
    public Result getDislikeCommentList(HttpServletRequest request) {
        return commentService.getDislikeCommentList(request);
    }
}
