package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.blog.pojo.request.CommentLikeRequest;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.common.enums.CommentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("comment")
public class CommentDO {

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    private Long commentLike;

    private Long commentDislike;

    private Long parentId;

    private String commentNetname;

    private String commentUsername;

    private String commentContent;

    private Long refrenceId;

    private CommentTypeEnum commentType;

    private LocalDateTime createTime;

    private String parentUsername;

    private String parentNetname;

    private String commentAvator;

    public static CommentDO buildDailyDOByLikeRequest(CommentLikeRequest commentLikeRequest) {
        return new CommentDO()
                .setCommentId(commentLikeRequest.getCommentId())
                .setCommentLike(commentLikeRequest.getCommentLike())
                .setCommentDislike(commentLikeRequest.getCommentDislike())
                .setCommentContent(commentLikeRequest.getCommentContent())
                .setCommentAvator(commentLikeRequest.getCommentAvator())
                .setCommentUsername(commentLikeRequest.getCommentUsername())
                .setCommentNetname(commentLikeRequest.getCommentNetname());
    }

    public static CommentDO buildCommentDO(CommentRequest commentRequest, BlogUserDO blogUserDO) {
        return new CommentDO()
                .setCommentLike(0L)
                .setCommentDislike(0L)
                .setParentId(commentRequest.getParentId())
                .setCommentNetname(blogUserDO.getUserNetname())
                .setCommentUsername(blogUserDO.getUserName())
                .setCommentContent(commentRequest.getCommentContent())
                .setRefrenceId(commentRequest.getRefrenceId())
                .setCommentType(CommentTypeEnum.DAILY)
                .setCreateTime(LocalDateTime.now())
                .setParentUsername(commentRequest.getParentUsername())
                .setParentNetname(commentRequest.getParentNetname())
                .setCommentAvator(blogUserDO.getUserAvator());
    }

    public static CommentDO buildCommentDOArticle(CommentRequest commentRequest, BlogUserDO blogUserDO) {
        return new CommentDO()
                .setCommentLike(0L)
                .setCommentDislike(0L)
                .setParentId(commentRequest.getParentId())
                .setCommentNetname(blogUserDO.getUserNetname())
                .setCommentUsername(blogUserDO.getUserName())
                .setCommentContent(commentRequest.getCommentContent())
                .setRefrenceId(commentRequest.getRefrenceId())
                .setCommentType(CommentTypeEnum.ARTICLE)
                .setCreateTime(LocalDateTime.now())
                .setParentUsername(commentRequest.getParentUsername())
                .setParentNetname(commentRequest.getParentNetname())
                .setCommentAvator(blogUserDO.getUserAvator());
    }
}
