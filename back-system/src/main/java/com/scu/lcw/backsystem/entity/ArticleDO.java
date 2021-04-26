package com.scu.lcw.backsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.backsystem.pojo.request.ArticleRequest;
import com.scu.lcw.common.enums.ArticleStatusEnum;
import com.scu.lcw.common.enums.ArticleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    public static ArticleDO buildArticle(ArticleRequest articleRequest) {
        System.out.println(articleRequest);
        return new ArticleDO()
                .setArticleTitle(articleRequest.getTitle())
                .setArticleDescription(articleRequest.getDescription())
                .setArticleLabel(articleRequest.getLabel())
                .setCoverImage(articleRequest.getCoverImgUrl())
                .setFirstImage(articleRequest.getFirstImgUrl())
                .setCreateTime(LocalDateTime.now())
                .setArticleLike(0L)
                .setArticleDislike(0L)
                .setArticleRank(0.0)
                .setArticleContent(articleRequest.getContent())
                .setArticleClick(0L)
                .setArticleType(articleRequest.getTypeEnum())
                .setArticleStatus(ArticleStatusEnum.DRAFT)
                .setArticlePassword(articleRequest.getPassword());
    }

}
