package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.blog.mapper.CommentMapper;
import com.scu.lcw.blog.pojo.dto.CommentDTO;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.blog.pojo.request.CommentTypeEnum;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 15:41
 */
@Component
@Slf4j
public class CommentServiceImpl extends BaseController implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private BlogUserMapper blogUserMapper;

    @Override
    public List<CommentDTO> findDailyComment(DailyDO dailyDO) {
        //获取父节点
        List<CommentDTO> dailyComments = convertParent(findAllDailyComment(dailyDO))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .collect(Collectors.toList());

        //拼装子节点
        dailyComments.stream()
                .forEach(this::convertChild);

        return dailyComments;
    }

    @Override
    public List<CommentDTO> findArticleComment(Long articleId) {
        //获取父节点
        List<CommentDTO> articleComments = convertParent(findAllArticleComment(articleId))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .collect(Collectors.toList());

        //拼装子节点
        articleComments.stream()
                .forEach(this::convertChild);

        return articleComments;
    }

    @Override
    public Result addDailyComment(CommentRequest commentRequest) {
        int insertResult = commentMapper.insert(CommentDO.buildCommentDO(commentRequest, this.getBlogUserMessage(commentRequest.getBlogUserLoginFlag())));
        return Result.data(insertResult);
    }

    @Override
    @Transactional
    public synchronized Result likeComment(CommentDO commentDO, String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeComment = blogUserMessage.getUserLikeComment();
        String userDislikeComment = blogUserMessage.getUserDislikeComment();
        if (StringUtils.isEmpty(userLikeComment)) {
            blogUserMapper.update(
                    blogUserMessage.setUserLikeComment(String.valueOf(commentDO.getCommentId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            commentMapper.update(commentDO.setCommentLike(commentDO.getCommentLike() + 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
            decreDislike(commentDO, userDislikeComment, blogUserMessage);
            return Result.ok();
        }
        increLike(commentDO, userLikeComment, blogUserMessage);
        decreDislike(commentDO, userDislikeComment, blogUserMessage);
        return Result.ok();
    }

    @Override
    @Transactional
    public synchronized Result dislikeComment(CommentDO commentDO, String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeComment = blogUserMessage.getUserLikeComment();
        String userDislikeComment = blogUserMessage.getUserDislikeComment();
        if (StringUtils.isEmpty(userDislikeComment)) {
            blogUserMapper.update(
                    blogUserMessage.setUserDislikeComment(String.valueOf(commentDO.getCommentId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            commentMapper.update(commentDO.setCommentDislike(commentDO.getCommentDislike() + 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
            decreLike(commentDO, userLikeComment, blogUserMessage);
            return Result.ok();
        }
        increDislike(commentDO, userDislikeComment, blogUserMessage);
        decreLike(commentDO, userLikeComment, blogUserMessage);
        return Result.ok();
    }

    @Override
    public Result getLikeCommentList(String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        String likeComment = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserLikeComment();
        if (StringUtils.isEmpty(likeComment)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(likeComment.split(",")));
    }

    @Override
    public Result getDislikeCommentList(String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        String dislikeComment = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserDislikeComment();
        if (StringUtils.isEmpty(dislikeComment)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(dislikeComment.split(",")));
    }

    private void increDislike(CommentDO commentDO, String userDislikeComment, BlogUserDO blogUser) {
        List<String> commentDislike = Arrays.asList(userDislikeComment.split(","));
        if (commentDislike.contains(String.valueOf(commentDO.getCommentId()))) {
            commentMapper.update(commentDO.setCommentDislike(commentDO.getCommentDislike() - 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
            blogUserMapper.update(blogUser.setUserDislikeComment(commentDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(commentDO.getCommentId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userDislikeComment = userDislikeComment + "," + commentDO.getCommentId();
        blogUserMapper.update(blogUser.setUserDislikeComment(userDislikeComment), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        commentMapper.update(commentDO.setCommentDislike(commentDO.getCommentDislike() + 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
        return;
    }

    private void increLike(CommentDO commentDO, String userLikeComment, BlogUserDO blogUser) {
        List<String> commentLike = Arrays.asList(userLikeComment.split(","));
        if (commentLike.contains(String.valueOf(commentDO.getCommentId()))) {
            commentMapper.update(commentDO.setCommentLike(commentDO.getCommentLike() - 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
            blogUserMapper.update(blogUser.setUserLikeComment(commentLike.stream()
                            .filter(like -> !like.equals(String.valueOf(commentDO.getCommentId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userLikeComment = userLikeComment + "," + commentDO.getCommentId();
        blogUserMapper.update(blogUser.setUserLikeComment(userLikeComment), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        commentMapper.update(commentDO.setCommentLike(commentDO.getCommentLike() + 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
        return;
    }

    private void decreDislike(CommentDO commentDO, String userDislikeComment, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userDislikeComment)) {
            return;
        }
        List<String> commentDislike = Arrays.asList(userDislikeComment.split(","));
        if (commentDislike.contains(String.valueOf(commentDO.getCommentId()))) {
            blogUserMapper.update(blogUser.setUserDislikeComment(commentDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(commentDO.getCommentId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            commentMapper.update(commentDO.setCommentDislike(commentDO.getCommentDislike() - 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
        }
        return;
    }

    private void decreLike(CommentDO commentDO, String userLikeComment, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userLikeComment)) {
            return;
        }
        List<String> commentLike = Arrays.asList(userLikeComment.split(","));
        if (commentLike.contains(String.valueOf(commentDO.getCommentId()))) {
            blogUserMapper.update(blogUser.setUserLikeComment(commentLike.stream()
                            .filter(like -> !like.equals(String.valueOf(commentDO.getCommentId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            commentMapper.update(commentDO.setCommentLike(commentDO.getCommentLike() - 1L), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId()));
        }
        return;
    }

    private List<CommentDO> findAllDailyComment(DailyDO dailyDO) {
        return commentMapper.selectList(new QueryWrapper<CommentDO>()
                .eq("refrence_id", dailyDO.getDailyId())
                .eq("comment_type", CommentTypeEnum.DAILY));
    }

    private List<CommentDO> findAllArticleComment(Long articleId) {
        return commentMapper.selectList(new QueryWrapper<CommentDO>()
                .eq("refrence_id", articleId)
                .eq("comment_type", CommentTypeEnum.ARTICLE));
    }

    private List<CommentDO> convertParent(List<CommentDO> commentList) {
        return commentList.stream()
                .filter(commentDO -> commentDO.getParentId().equals(0L))
                .sorted(Comparator.comparing(CommentDO::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    private void convertChild(CommentDTO parentComment) {
        List<CommentDTO> childList = commentMapper.selectList(
                new QueryWrapper<CommentDO>()
                        .eq("parent_id", parentComment.getCommentId()))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .sorted(Comparator.comparing(CommentDTO::getCreateTime).reversed())
                .collect(Collectors.toList());

        if (childList.size() == 0) {
            return;
        }

        parentComment.setChildList(childList);
    }
}
