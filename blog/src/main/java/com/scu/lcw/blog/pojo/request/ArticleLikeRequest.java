package com.scu.lcw.blog.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleLikeRequest {

    private Long articleId;

    private Long articleLike;

    private Long articleDislike;

    private String articleContent;

    private String articleAvator;

    private String articleUsername;

    private String articleNetname;

    private Integer commentLength;

    private String date;

    private String dateTime;

    private String blogUserLoginFlag;

}
