package com.scu.lcw.backsystem.controller;

import com.scu.lcw.backsystem.Service.ArticleService;
import com.scu.lcw.backsystem.pojo.request.ArticleRequest;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/articlemng")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping("/add")
    public Result addArticle(ArticleRequest articleRequest) {
        return articleService.addNewArticle(articleRequest);
    }

    @RequestMapping("/edit")
    public Result editArticle(ArticleRequest articleRequest) {
        return articleService.editNewArticle(articleRequest);
    }

    @RequestMapping("/config")
    public Result getArticleConfig() {
        return articleService.getArticleConfig();
    }

    @RequestMapping("/delete")
    public Result deleteArticle(ArticleRequest articleRequest) {
        return articleService.delete(articleRequest);
    }
}
