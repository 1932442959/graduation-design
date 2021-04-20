package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.ReportDO;
import com.scu.lcw.blog.mapper.ReportMapper;
import com.scu.lcw.blog.pojo.request.ReportRequest;
import com.scu.lcw.blog.service.ReportService;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:26
 */
@Slf4j
@Component
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private PageUtils<ReportDO> pageUtils;

    @Override
    public Result findAllReport(ReportRequest reportRequest) {
        return Result.data(
                pageUtils.listPagination(
                        reportMapper.selectList(new QueryWrapper<>())
                                .stream()
                                .sorted(Comparator.comparing(ReportDO::getCreateTime).reversed())
                                .collect(Collectors.toList()),
                        reportRequest.getCurrentPage(),
                        reportRequest.getPageSize()
                )
        );
    }
}