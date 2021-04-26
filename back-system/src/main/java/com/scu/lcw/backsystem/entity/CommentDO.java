package com.scu.lcw.backsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

}
