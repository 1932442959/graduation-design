package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.request.ArticleLikeRequest;
import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.blog.pojo.request.DailyLikeRequest;
import com.scu.lcw.blog.pojo.request.LoginMessageRequest;
import com.scu.lcw.blog.service.ArticleService;
import com.scu.lcw.blog.util.AntiBrushUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Resource
    private ArticleService articleService;

    @Resource
    private AntiBrushUtils antiBrushUtils;

    @RequestMapping("/addvisit")
    public void addVisit(ArticleRequest articleRequest){
        articleService.addVisit(articleRequest);
    }

    @RequestMapping("/getnew")
    public Result getNew(ArticleRequest articleRequest) {
        return articleService.getNewArticleList(articleRequest);
    }

    @RequestMapping("/getlist")
    public Result getArticleList(ArticleRequest articleRequest) {
        return articleService.getArticleList(articleRequest);
    }

    @RequestMapping("/getcomment")
    public Result getArticleComment(ArticleRequest articleRequest) {
        return articleService.getArticleList(articleRequest);
    }

    @RequestMapping("/searcharticle")
    public Result searchArticle(ArticleRequest articleRequest) {
        return articleService.searchArticle(articleRequest);
    }

    @PostMapping("/likearticle")
    public Result likeDaily(ArticleLikeRequest articleLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(articleLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(articleLikeRequest.getBlogUserLoginFlag())) {
            Result result = articleService.likeArticle(ArticleDO.buildArticleDO(articleLikeRequest), articleLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(articleLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @PostMapping("/dislikearticle")
    public Result dislikeDaily(ArticleLikeRequest articleLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(articleLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(articleLikeRequest.getBlogUserLoginFlag())) {
            Result result = articleService.dislikeArticle(ArticleDO.buildArticleDO(articleLikeRequest), articleLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(articleLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/likearticlelist")
    public Result getLikeDailyList(LoginMessageRequest loginMessageRequest) {
        return articleService.getLikeArticleList(loginMessageRequest.getBlogUserLoginFlag());
    }

    @RequestMapping("/dislikearticlelist")
    public Result getDislikeDailyList(LoginMessageRequest loginMessageRequest) {
        return articleService.getDislikeArticleList(loginMessageRequest.getBlogUserLoginFlag());
    }
}
