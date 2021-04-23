package com.scu.lcw.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentLikeRequest {

    private Long commentId;

    private Long commentLike;

    private Long commentDislike;

    private String commentContent;

    private String commentAvator;

    private String commentUsername;

    private String commentNetname;

    private Integer commentLength;

    private String date;

    private String dateTime;

}
