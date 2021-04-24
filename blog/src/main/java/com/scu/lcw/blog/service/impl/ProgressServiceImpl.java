package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.ProgressDO;
import com.scu.lcw.blog.mapper.ProgressMapper;
import com.scu.lcw.blog.pojo.request.ProgressRequest;
import com.scu.lcw.blog.pojo.vo.ProgressVO;
import com.scu.lcw.blog.service.ProgressService;
import com.scu.lcw.blog.util.LocalDateUtils;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProgressServiceImpl implements ProgressService {

    @Resource
    private ProgressMapper progressMapper;

    @Resource
    private PageUtils pageUtils;

    @Resource
    private LocalDateUtils localDateUtils;

    @Override
    public Result getProgressList(ProgressRequest progressRequest) {
        List<ProgressDO> progressList = progressMapper.selectList(new QueryWrapper<>())
                .stream()
                .sorted(Comparator.comparing(ProgressDO::getCreateTime).reversed())
                .map(progressDO -> progressDO.setDate(localDateUtils.parseCreateTime(progressDO.getCreateTime()))
                        .setDateTime(localDateUtils.dateTimeHHmmss(progressDO.getCreateTime())))
                .collect(Collectors.toList());
        return Result.data(
                new ProgressVO()
                        .setProgressList(pageUtils.listPagination(
                                progressList,
                                progressRequest.getCurrentPage(),
                                progressRequest.getPageSize()
                        ))
                        .setTotal(progressList.size())
        );
    }
}
