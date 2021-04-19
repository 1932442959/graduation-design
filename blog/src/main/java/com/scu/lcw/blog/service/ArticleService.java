package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.common.response.Result;

public interface ArticleService {

    Result getArticleList(ArticleRequest articleRequest);

}
