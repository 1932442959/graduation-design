package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.LabelDO;
import com.scu.lcw.blog.mapper.ArticleMapper;
import com.scu.lcw.blog.mapper.LabelMapper;
import com.scu.lcw.blog.pojo.request.ArticleRequest;
import com.scu.lcw.blog.pojo.request.LabelRequest;
import com.scu.lcw.blog.pojo.vo.ArticleVO;
import com.scu.lcw.blog.pojo.vo.LabelVO;
import com.scu.lcw.blog.service.ArticleService;
import com.scu.lcw.blog.service.LabelService;
import com.scu.lcw.common.response.RedisKeyName;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelMapper labelMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ArticleService articleService;

    @Override
    public Result getLabelList() {
        List<LabelDO> labelList = labelMapper.selectList(new QueryWrapper<LabelDO>().ne("parent_id", 0L));
        return Result.data(labelList);
    }

    @Override
    public Result getAllLabelList() {
        List<LabelDO> parentLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>().eq("parent_id", 0L));
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>().ne("parent_id", 0L));
        //将子节点拼装到对应根节点标签
        List<LabelVO> resultList = parentLabelList.stream()
                .map(labelDO -> labelDO.setArticleNum(this.fingArticleNum(labelDO)))
                .map(LabelVO::buildLabelVO)
                .map(labelVO ->
                        labelVO.setChildren(
                                childLabelList.stream()
                                        .map(LabelVO::buildLabelVO)
                                        .filter(childLabel -> childLabel.getParentId().equals(labelVO.getLabelId()))
                                        .collect(Collectors.collectingAndThen(
                                                Collectors.toList(), labelVOS -> labelVOS.stream()
                                                        .map(vo -> {
                                                            ArticleRequest articleRequest = new ArticleRequest(vo.getLabelName(), 1, 10000);
                                                            ArticleVO articleVO = (ArticleVO) articleService.getArticleList(articleRequest).getData();
                                                            List<LabelVO> children = articleVO.getArticleList().stream()
                                                                    .map(LabelVO::buildArticleChild)
                                                                    .collect(Collectors.toList());
                                                            return vo.setChildren(children);
                                                        })
                                                        .collect(Collectors.toList())
                                                )
                                        )
                        )
                )
                .collect(Collectors.toList());
        return Result.data(resultList);
    }

    @Override
    public Result addParent(LabelRequest labelRequest) {
        return Result.data(labelMapper.insert(new LabelDO()
                .setParentId(0L)
                .setLabelName(labelRequest.getLabelName())
                .setLabelColor(labelRequest.getLabelColor())));
    }

    @Override
    public Result addChild(LabelRequest labelRequest) {
        return Result.data(labelMapper.insert(new LabelDO()
                .setLabelColor(labelRequest.getLabelColor())
                .setLabelName(labelRequest.getLabelName())
                .setParentId(labelRequest.getParentId())));
    }

    @Override
    public Result update(LabelRequest labelRequest) {
        return Result.data(labelMapper.update(new LabelDO()
                        .setParentId(labelRequest.getParentId())
                        .setLabelName(labelRequest.getLabelName())
                        .setLabelColor(labelRequest.getLabelColor())
                , new QueryWrapper<LabelDO>().eq("label_id", labelRequest.getLabelId())));
    }

    @Override
    public Result delete(LabelRequest labelRequest) {
        return Result.data(labelMapper.delete(new QueryWrapper<LabelDO>().eq("label_id", labelRequest.getLabelId())));
    }

    @Resource
    private ArticleMapper articleMapper;

    private Integer fingArticleNum(LabelDO label) {
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>()
                .eq("parent_id", label.getLabelId()));

        if (childLabelList.size() == 1) {
            return articleMapper.selectList(
                    new QueryWrapper<ArticleDO>()
                            .like("article_label", childLabelList.get(0).getLabelName())).size();
        }

        return childLabelList.stream()
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
                .collect(Collectors.toList()).size();
    }
}
