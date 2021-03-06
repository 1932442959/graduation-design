package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.blog.pojo.bo.ArticleBO;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.ArticleLikeRequest;
import com.scu.lcw.blog.pojo.request.DailyLikeRequest;
import com.scu.lcw.common.enums.ArticleStatusEnum;
import com.scu.lcw.common.enums.ArticleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import sun.java2d.pipe.AAShapePipe;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("article")
public class ArticleDO {

    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    private String articleTitle;

    private String articleDescription;

    private String articleLabel;

    private String coverImage;

    private String firstImage;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long articleLike;

    private Long articleDislike;

    private Double articleRank;

    private String articleContent;

    private Long articleClick;

    private ArticleTypeEnum articleType;

    private ArticleStatusEnum articleStatus;

    private String articlePassword;

    @TableField(exist = false)
    private String articleCreateTime;

    @TableField(exist = false)
    private String articleUpdateTime;

    @TableField(exist = false)
    private Integer commentLength;

    @TableField(exist = false)
    List<LabelDO> labelList;

    public static ArticleBO buildArticleBO(ArticleDO articleDO) {
        return new ArticleBO().setArticleDO(articleDO);
    }

    public static ArticleDO buildArticleDO(ArticleLikeRequest articleLikeRequest) {
        return new ArticleDO()
                .setArticleId(articleLikeRequest.getArticleId())
                .setArticleLike(articleLikeRequest.getArticleLike())
                .setArticleDislike(articleLikeRequest.getArticleDislike());
    }

}
