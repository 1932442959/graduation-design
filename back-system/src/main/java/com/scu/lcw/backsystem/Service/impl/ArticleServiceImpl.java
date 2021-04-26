package com.scu.lcw.backsystem.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.backsystem.Service.ArticleService;
import com.scu.lcw.backsystem.entity.ArticleDO;
import com.scu.lcw.backsystem.entity.LabelDO;
import com.scu.lcw.backsystem.mapper.ArticleMapper;
import com.scu.lcw.backsystem.mapper.LabelMapper;
import com.scu.lcw.backsystem.pojo.request.ArticleRequest;
import com.scu.lcw.backsystem.pojo.vo.ArticleConfigVO;
import com.scu.lcw.common.enums.ArticleTypeEnum;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private LabelMapper labelMapper;

    @Override
    @Transactional
    public Result addNewArticle(ArticleRequest articleRequest) {
        return Result.data(articleMapper.insert(ArticleDO.buildArticle(articleRequest)));
    }

    @Override
    public Result editNewArticle(ArticleRequest articleRequest) {
        return Result.data(articleMapper.update(ArticleDO.buildArticle(articleRequest), new QueryWrapper<ArticleDO>().eq("article_id", articleRequest.getArticleId())));
    }

    @Override
    public Result getArticleConfig() {
        return Result.data(new ArticleConfigVO()
                .setLabelList(labelMapper.selectList(new QueryWrapper<LabelDO>()
                        .ne("parent_id", 0L))
                        .stream()
                        .map(LabelDO::getLabelName)
                        .collect(Collectors.toList()))
        );
    }

    @Override
    public Result delete(ArticleRequest articleRequest) {
        return Result.data(articleMapper.delete(new QueryWrapper<ArticleDO>()
                .eq("article_id", articleRequest.getArticleId())));
    }
}
