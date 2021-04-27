package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.entity.LabelDO;
import com.scu.lcw.blog.mapper.ArticleMapper;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.blog.mapper.LabelMapper;
import com.scu.lcw.blog.pojo.bo.ArticleBO;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.blog.pojo.vo.ArticleVO;
import com.scu.lcw.blog.pojo.vo.DailyVO;
import com.scu.lcw.blog.service.ArticleService;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.util.LocalDateUtils;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleServiceImpl extends BaseController implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private LabelMapper labelMapper;

    @Resource
    private LocalDateUtils localDateUtils;

    @Resource
    private CommentService commentService;

    @Resource
    private PageUtils pageUtils;

    @Resource
    private BlogUserMapper blogUserMapper;

    @Override
    public Result getArticleList(ArticleRequest articleRequest) {
        if (articleRequest.getArticleId() != null) {
            return findOneArticle(articleRequest);
        }

        if (StringUtils.isEmpty(articleRequest.getLabelName())) {
            return findAllArticle(articleRequest);
        }

        LabelDO label = labelMapper.selectList(new QueryWrapper<LabelDO>().eq("label_name", articleRequest.getLabelName())).get(0);

        if (label.getParentId().equals(0L)) {
            return findArticleByParentLabel(label, articleRequest);
        }

        List<ArticleDO> articleList = articleMapper.selectList(
                new QueryWrapper<ArticleDO>()
                        .like("article_label", label.getLabelName()))
                .stream()
                .filter(articleDO -> Arrays.asList(articleDO.getArticleLabel().split(",")).contains(label.getLabelName()))
                .collect(Collectors.toList());

        return addArticleCommentBySearch(
                convertArticle(articleList, articleRequest)
                , articleList.size());
    }

    @Override
    public Result searchArticle(ArticleRequest articleRequest) {
        List<ArticleDO> articleList = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .like("article_title", articleRequest.getLabelName())
                .or()
                .like("article_label", articleRequest.getLabelName()))
                .stream()
                .sorted(Comparator.comparing(ArticleDO::getCreateTime).reversed())
                .collect(Collectors.toList());

        return addArticleCommentBySearch(pageUtils.listPagination(articleList,
                articleRequest.getCurrentPage(),
                articleRequest.getPageSize())
                , articleList.size());
    }

    @Override
    public Result getNewArticleList(ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleMapper.selectList(new QueryWrapper<>());
        return addArticleComment(convertNewArticle(resultList, articleRequest));
    }

    @Override
    @Transactional
    public Result likeArticle(ArticleDO articleDO, String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeDaily = blogUserMessage.getUserLikeArticle();
        String userDislikeDaily = blogUserMessage.getUserDislikeArticle();
        if (StringUtils.isEmpty(userLikeDaily)) {
            blogUserMapper.update(
                    blogUserMessage.setUserLikeArticle(String.valueOf(articleDO.getArticleId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            articleMapper.update(articleDO.setArticleLike(articleDO.getArticleLike() + 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
            decreDislike(articleDO, userDislikeDaily, blogUserMessage);
            return Result.ok();
        }
        increLike(articleDO, userLikeDaily, blogUserMessage);
        decreDislike(articleDO, userDislikeDaily, blogUserMessage);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result dislikeArticle(ArticleDO articleDO, String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeDaily = blogUserMessage.getUserLikeArticle();
        String userDislikeDaily = blogUserMessage.getUserDislikeArticle();
        if (StringUtils.isEmpty(userDislikeDaily)) {
            blogUserMapper.update(
                    blogUserMessage.setUserDislikeArticle(String.valueOf(articleDO.getArticleId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            articleMapper.update(articleDO.setArticleDislike(articleDO.getArticleDislike() + 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
            decreLike(articleDO, userLikeDaily, blogUserMessage);
            return Result.ok();
        }
        increDislike(articleDO, userDislikeDaily, blogUserMessage);
        decreLike(articleDO, userLikeDaily, blogUserMessage);
        return Result.ok();
    }

    @Override
    public Result getLikeArticleList(String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        String likeDaily = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserLikeArticle();
        if (StringUtils.isEmpty(likeDaily)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(likeDaily.split(",")));
    }

    @Override
    public Result getDislikeArticleList(String blogUserLoginFlag) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(blogUserLoginFlag);
        String dislikeDaily = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserDislikeArticle();
        if (StringUtils.isEmpty(dislikeDaily)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(dislikeDaily.split(",")));
    }

    @Override
    public void addVisit(ArticleRequest articleRequest) {
        ArticleDO article = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .eq("article_id", articleRequest.getArticleId())).get(0);
        articleMapper.update(article.setArticleClick(article.getArticleClick() + 1L), new QueryWrapper<ArticleDO>().eq("article_id", article.getArticleId()));
    }

    private void increDislike(ArticleDO articleDO, String userDislikeDaily, BlogUserDO blogUser) {
        List<String> dailyDislike = Arrays.asList(userDislikeDaily.split(","));
        if (dailyDislike.contains(String.valueOf(articleDO.getArticleId()))) {
            articleMapper.update(articleDO.setArticleDislike(articleDO.getArticleDislike() - 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
            blogUserMapper.update(blogUser.setUserDislikeArticle(dailyDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(articleDO.getArticleId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userDislikeDaily = userDislikeDaily + "," + articleDO.getArticleId();
        blogUserMapper.update(blogUser.setUserDislikeArticle(userDislikeDaily), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        articleMapper.update(articleDO.setArticleDislike(articleDO.getArticleDislike() + 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
        return;
    }

    private void increLike(ArticleDO articleDO, String userLikeDaily, BlogUserDO blogUser) {
        List<String> dailyLike = Arrays.asList(userLikeDaily.split(","));
        if (dailyLike.contains(String.valueOf(articleDO.getArticleId()))) {
            articleMapper.update(articleDO.setArticleLike(articleDO.getArticleLike() - 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
            blogUserMapper.update(blogUser.setUserLikeArticle(dailyLike.stream()
                            .filter(like -> !like.equals(String.valueOf(articleDO.getArticleId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userLikeDaily = userLikeDaily + "," + articleDO.getArticleId();
        blogUserMapper.update(blogUser.setUserLikeArticle(userLikeDaily), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        articleMapper.update(articleDO.setArticleLike(articleDO.getArticleLike() + 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
        return;
    }

    private void decreDislike(ArticleDO articleDO, String userDislikeDaily, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userDislikeDaily)) {
            return;
        }
        List<String> dailyDislike = Arrays.asList(userDislikeDaily.split(","));
        if (dailyDislike.contains(String.valueOf(articleDO.getArticleId()))) {
            blogUserMapper.update(blogUser.setUserDislikeArticle(dailyDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(articleDO.getArticleId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            articleMapper.update(articleDO.setArticleDislike(articleDO.getArticleDislike() - 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
        }
        return;
    }

    private void decreLike(ArticleDO articleDO, String userLikeDaily, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userLikeDaily)) {
            return;
        }
        List<String> dailyLike = Arrays.asList(userLikeDaily.split(","));
        if (dailyLike.contains(String.valueOf(articleDO.getArticleId()))) {
            blogUserMapper.update(blogUser.setUserLikeArticle(dailyLike.stream()
                            .filter(like -> !like.equals(String.valueOf(articleDO.getArticleId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            articleMapper.update(articleDO.setArticleLike(articleDO.getArticleLike() - 1L), new QueryWrapper<ArticleDO>().eq("article_id", articleDO.getArticleId()));
        }
        return;
    }

    private Result addArticleCommentBySearch(List<ArticleDO> articleList, Integer total) {
        List<ArticleBO> allArticle = articleList.stream()
                .map(ArticleDO::buildArticleBO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), articles ->
                                articles.stream()
                                        .map(articleBO -> articleBO
                                                .setCommentList(
                                                        commentService.findArticleComment(articleBO.getArticleDO().getArticleId())
                                                )
                                        )

                        )
                )
                .map(this::computeCommentLength)
                .collect(Collectors.toList());

        return Result.data(new ArticleVO()
                .setArticleList(allArticle)
                .setTotal(total));
    }

    private Result addArticleComment(List<ArticleDO> articleList) {
        List<ArticleBO> allArticle = articleList.stream()
                .map(ArticleDO::buildArticleBO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), articles ->
                                articles.stream()
                                        .map(articleBO -> articleBO
                                                .setCommentList(
                                                        commentService.findArticleComment(articleBO.getArticleDO().getArticleId())
                                                )
                                        )

                        )
                )
                .map(this::computeCommentLength)
                .collect(Collectors.toList());

        return Result.data(new ArticleVO()
                .setArticleList(allArticle)
                .setTotal(getTotalDaily()));
    }

    private Result findArticleByParentLabel(LabelDO label, ArticleRequest articleRequest) {
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>()
                .eq("parent_id", label.getLabelId()));

        if (childLabelList.size() == 1) {
            articleMapper.selectList(
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName()));

            return addArticleComment(convertArticle(articleMapper.selectList(
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName()))
                    , articleRequest));
        }

        List<ArticleDO> articleList = childLabelList.stream()
                .map(LabelDO::getLabelName)
                .map(labelName -> articleMapper.selectList(
                        new QueryWrapper<ArticleDO>()
                                .like("article_label", labelName))
                        .stream()
                        .filter(articleDO -> Arrays.asList(articleDO.getArticleLabel().split(",")).contains(labelName))
                        .collect(Collectors.toList())
                )
                .flatMap(List::stream)
                .distinct()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(localDateUtils.parseCreateTime(articleDO.getCreateTime()))
                )
                .collect(Collectors.toList());

        return addArticleCommentBySearch(convertArticle(articleList, articleRequest), articleList.size());
    }

    private Result findOneArticle(ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .eq("article_id", articleRequest.getArticleId()));
        return addArticleComment(convertArticle(resultList, articleRequest));
    }

    private Result findAllArticle(ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleMapper.selectList(new QueryWrapper<>());
        return addArticleComment(convertArticle(resultList, articleRequest));
    }

    private List<ArticleDO> convertNewArticle(List<ArticleDO> articleList, ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleList.stream()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(localDateUtils.parseCreateTime(articleDO.getCreateTime()))
                )
                .sorted(Comparator.comparing(ArticleDO::getCreateTime).reversed())
                .map(this::convertArticleLabel)
                .distinct()
                .collect(Collectors.toList());

        return pageUtils.listPagination(resultList, articleRequest.getCurrentPage(), articleRequest.getPageSize());
    }

    private List<ArticleDO> convertArticle(List<ArticleDO> articleList, ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleList.stream()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(localDateUtils.parseCreateTime(articleDO.getCreateTime()))
                )
                .sorted(Comparator.comparing(ArticleDO::getArticleLike).reversed())
                .map(this::convertArticleLabel)
                .distinct()
                .collect(Collectors.toList());

        return pageUtils.listPagination(resultList, articleRequest.getCurrentPage(), articleRequest.getPageSize());
    }

    private Integer getTotalDaily() {
        return articleMapper.selectCount(new QueryWrapper<>());
    }


    private ArticleBO computeCommentLength(ArticleBO articleBO) {
        articleBO.getArticleDO().setCommentLength(articleBO.getCommentList().size());
        return articleBO;
    }

    private ArticleDO convertArticleLabel(ArticleDO articleDO) {
        String articleLabel = articleDO.getArticleLabel();
        if (StringUtils.isEmpty(articleLabel)) {
            return articleDO;
        }
        if (!articleLabel.contains(",")) {
            List<LabelDO> labelList = new ArrayList<>();
            labelList.add(labelMapper.selectList(new QueryWrapper<LabelDO>()
                    .eq("label_name", articleDO.getArticleLabel()))
                    .get(0));
            return articleDO.setLabelList(labelList);
        }
        List<LabelDO> labelList = Arrays.asList(articleLabel.split(","))
                .stream()
                .map(
                        labelName -> labelMapper.selectList(new QueryWrapper<LabelDO>()
                                .eq("label_name", labelName))
                                .get(0)
                )
                .collect(Collectors.toList());

        return articleDO.setLabelList(labelList);
    }
}
