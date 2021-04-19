package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.LabelDO;
import com.scu.lcw.blog.mapper.LabelMapper;
import com.scu.lcw.blog.pojo.vo.LabelVO;
import com.scu.lcw.blog.service.LabelService;
import com.scu.lcw.common.response.RedisKeyName;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Override
    public Result getLabelList() {
        String redisKey = RedisKeyName.PARENT_LABEL_LIST;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(redisKey) != null) {
            return Result.data(valueOperations.get(redisKey));
        }
        List<LabelDO> labelList = labelMapper.selectList(new QueryWrapper<LabelDO>().ne("parent_id", 0L));
        valueOperations.set(redisKey, labelList, 24L, TimeUnit.HOURS);
        return Result.data(labelList);
    }

    @Override
    public Result getAllLabelList() {

        String redisKey = RedisKeyName.ALL_LABEL_LIST;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(redisKey) != null) {
            return Result.data(valueOperations.get(redisKey));
        }

        List<LabelDO> parentLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>().eq("parent_id", 0L));
        List<LabelDO> childLabelList = labelMapper.selectList(new QueryWrapper<LabelDO>().ne("parent_id", 0L));
        //将子节点拼装到对应根节点标签
        List<LabelVO> resultList = parentLabelList.stream()
                .map(LabelVO::buildLabelVO)
                .map(labelVO ->
                        labelVO.setChildList(
                                childLabelList.stream()
                                        .map(LabelVO::buildLabelVO)
                                        .filter(childLabel -> childLabel.getParentId().equals(labelVO.getLabelId()))
                                        .collect(Collectors.toList())
                        )
                )
                .map(labelVO -> labelVO.setArticleNum(labelVO.getChildList().size()))
                .collect(Collectors.toList());
        valueOperations.set(redisKey, resultList, 24L, TimeUnit.HOURS);
        return Result.data(resultList);
    }
}
