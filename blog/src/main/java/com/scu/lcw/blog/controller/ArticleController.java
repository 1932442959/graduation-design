package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.blog.service.ArticleService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping("/getlist")
    public Result getArticleList(ArticleRequest articleRequest) {
        return articleService.getArticleList(articleRequest);
    }

    @RequestMapping("/getcomment")
    public Result getArticleComment(ArticleRequest articleRequest) {
        return articleService.getArticleList(articleRequest);
    }
}
