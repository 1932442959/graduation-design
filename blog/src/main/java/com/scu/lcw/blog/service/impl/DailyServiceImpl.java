package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.mapper.DailyMapper;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.blog.pojo.vo.DailyVO;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.service.DailyService;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:51
 */
@Slf4j
@Component
public class DailyServiceImpl implements DailyService {

    @Resource
    private PageUtils pageUtils;

    @Resource
    private DailyMapper dailyMapper;

    @Resource
    private CommentService commentService;

    @Override
    public Result findAllDaily(DailyRequest dailyRequest) {
        List<DailyBO> allDaily = findDailyPage(dailyRequest)
                .stream()
                .map(DailyDO::buildDailyVO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), dailyList ->
                                dailyList.stream()
                                        .map(dailyBO -> dailyBO
                                                .setCommentList(
                                                        commentService.findDailyComment(dailyBO.getDailyDO())
                                                )
                                        )

                        )
                ).collect(Collectors.toList());

        return Result.data(new DailyVO()
                .setDailyList(allDaily)
                .setTotal(allDaily.size()));
    }

    private List<DailyDO> findDailyPage(DailyRequest dailyRequest) {
        return pageUtils.listPagination(
                dailyMapper.selectList(new QueryWrapper<>())
                        .stream()
                        .sorted(Comparator.comparing(DailyDO::getCreateTime).reversed())
                        .collect(Collectors.toList()),
                dailyRequest.getCurrentPage(),
                dailyRequest.getPageSize()
        );
    }

}
