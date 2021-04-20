package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.LabelDO;
import com.scu.lcw.blog.mapper.ArticleMapper;
import com.scu.lcw.blog.mapper.LabelMapper;
import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.blog.pojo.vo.ArticleVO;
import com.scu.lcw.blog.service.ArticleService;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.util.LocalDateUtils;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleServiceImpl implements ArticleService {

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

    @Override
    public Result getArticleList(ArticleRequest articleRequest) {
        if (StringUtils.isEmpty(articleRequest.getLabelName())) {
            return findAllArticle(articleRequest);
        }

        LabelDO label = labelMapper.selectList(new QueryWrapper<LabelDO>().eq("label_name", articleRequest.getLabelName())).get(0);

        if (label.getParentId().equals(0L)) {
            return findArticleByParentLabel(label, articleRequest);
        }

        return convertArticle(articleMapper.selectList(
                new QueryWrapper<ArticleDO>()
                        .like("article_label", label.getLabelName()))
                , articleRequest);
    }

    @Override
    public Result getArticleComment(ArticleRequest articleRequest) {
        return Result.data(commentService.findArticleComment(articleRequest.getArticleId()));
    }

    private Result findArticleByParentLabel(LabelDO label, ArticleRequest articleRequest) {
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>()
                .eq("parent_id", label.getLabelId()));

        if (childLabelList.size() == 1) {
            articleMapper.selectList(
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName()));

            return convertArticle(articleMapper.selectList(
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName()))
                    , articleRequest);
        }

        List<ArticleDO> articleList = childLabelList.stream()
                .map(LabelDO::getLabelName)
                .map(labelName -> articleMapper.selectList(new QueryWrapper<ArticleDO>()
                        .like("article_label", labelName))
                )
                .flatMap(List::stream)
                .distinct()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(localDateUtils.parseCreateTime(articleDO.getCreateTime()))
                        .setArticleUpdateTime(localDateUtils.parseUpdateTime(articleDO.getUpdateTime()))
                )
                .collect(Collectors.toList());

        return convertArticle(articleList, articleRequest);
    }

    private Result findAllArticle(ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleMapper.selectList(new QueryWrapper<>());
        return convertArticle(resultList, articleRequest);
    }

    private Result convertArticle(List<ArticleDO> articleList, ArticleRequest articleRequest) {
        List<ArticleDO> resultList = articleList.stream()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(localDateUtils.parseCreateTime(articleDO.getCreateTime()))
                        .setArticleUpdateTime(localDateUtils.parseUpdateTime(articleDO.getUpdateTime()))
                )
                .sorted(Comparator.comparing(ArticleDO::getArticleLike).reversed())
                .collect(Collectors.toList());

        return Result.data(new ArticleVO()
                .setArticleList(pageUtils.listPagination(resultList, articleRequest.getCurrentPage(), articleRequest.getPageSize()))
                .setTotal(resultList.size()));
    }
}
