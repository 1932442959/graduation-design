package com.scu.lcw.blog.service;

import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.common.response.Result;

public interface ArticleService {

    Result getArticleList(ArticleRequest articleRequest);

    Result searchArticle(ArticleRequest articleRequest);

    Result getNewArticleList(ArticleRequest articleRequest);

    Result likeArticle(ArticleDO articleDO, String blogUserLoginFlag);

    Result dislikeArticle(ArticleDO articleDO, String blogUserLoginFlag);

    Result getLikeArticleList(String blogUserLoginFlag);

    Result getDislikeArticleList(String blogUserLoginFlag);

}
