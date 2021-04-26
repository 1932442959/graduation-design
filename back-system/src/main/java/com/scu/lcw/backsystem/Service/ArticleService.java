package com.scu.lcw.backsystem.Service;

import com.scu.lcw.backsystem.pojo.request.ArticleRequest;
import com.scu.lcw.common.response.Result;

public interface ArticleService {

    Result addNewArticle(ArticleRequest articleRequest);

    Result editNewArticle(ArticleRequest articleRequest);

    Result getArticleConfig();

    Result delete(ArticleRequest articleRequest);

}
