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
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private LabelMapper labelMapper;

    @Override
    public Result getArticleList(ArticleRequest articleRequest) {
        if (StringUtils.isEmpty(articleRequest.getLabelName())) {
            return findAllArticle(articleRequest);
        }

        LabelDO label = labelMapper.selectList(new QueryWrapper<LabelDO>().eq("label_name", articleRequest.getLabelName())).get(0);

        if (label.getParentId().equals(0L)) {
            return findArticleByParentLabel(label, articleRequest);
        }

        Page<ArticleDO> page = articleMapper.selectPage(
                new Page(articleRequest.getCurrentPage(), articleRequest.getPageSize()),
                new QueryWrapper<ArticleDO>()
                        .like("article_label", label.getLabelName()));

        return convertArticle(page);
    }

    private Result findArticleByParentLabel(LabelDO label, ArticleRequest articleRequest) {
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>()
                .eq("parent_id", label.getLabelId()));

        if (childLabelList.size() == 1) {
            Page<ArticleDO> page = articleMapper.selectPage(
                    new Page(articleRequest.getCurrentPage(), articleRequest.getPageSize()),
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName()));

            return convertArticle(page);
        }

        List<ArticleDO> articleList = childLabelList.stream()
                .map(LabelDO::getLabelName)
                .map(labelName -> articleMapper.selectList(new QueryWrapper<ArticleDO>()
                        .like("article_label", labelName))
                )
                .flatMap(List::stream)
                .distinct()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(articleDO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .setArticleUpdateTime(articleDO.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                )
                .limit(articleRequest.getPageSize())
                .collect(Collectors.toList());

        return Result.data(new ArticleVO()
                .setArticleList(articleList)
                .setTotal(articleList.stream().count()));
    }

    private Result findAllArticle(ArticleRequest articleRequest) {
        Page<ArticleDO> page = articleMapper.selectPage(
                new Page(articleRequest.getCurrentPage(), articleRequest.getPageSize()),
                new QueryWrapper<>()
        );

        return convertArticle(page);
    }

    private Result convertArticle(Page<ArticleDO> page) {
        List<ArticleDO> articleList = page.getRecords().stream()
                .map(articleDO -> articleDO
                        .setArticleCreateTime(articleDO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .setArticleUpdateTime(articleDO.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                )
                .collect(Collectors.toList());

        return Result.data(new ArticleVO()
                .setArticleList(articleList)
                .setTotal(page.getTotal()));
    }
}
