package com.scu.lcw.blog.pojo.dto;

import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.pojo.request.CommentTypeEnum;
import com.scu.lcw.blog.util.LocalDateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 15:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentDTO {

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

    private String commentAvator;

    private String parentUsername;

    private String parentNetname;

    private String date;

    private String dateTime;

    private boolean showReply;

    List<CommentDTO> childList;

    public static CommentDTO buildCommentDTO(CommentDO commentDO) {
        return new CommentDTO()
                .setCommentId(commentDO.getCommentId())
                .setCommentLike(commentDO.getCommentLike())
                .setCommentDislike(commentDO.getCommentDislike())
                .setParentId(commentDO.getParentId())
                .setCommentNetname(commentDO.getCommentNetname())
                .setCommentUsername(commentDO.getCommentUsername())
                .setCommentContent(commentDO.getCommentContent())
                .setRefrenceId(commentDO.getRefrenceId())
                .setCommentType(commentDO.getCommentType())
                .setCreateTime(commentDO.getCreateTime())
                .setCommentAvator(commentDO.getCommentAvator())
                .setParentNetname(commentDO.getParentNetname())
                .setParentUsername(commentDO.getParentUsername())
                .setDate(new LocalDateUtils().parseCreateTime(commentDO.getCreateTime()))
                .setDateTime(new LocalDateUtils().dateTimeHHmmss(commentDO.getCreateTime()));
    }
}
