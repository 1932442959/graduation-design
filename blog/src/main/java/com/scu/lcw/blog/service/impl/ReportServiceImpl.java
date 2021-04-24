package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.ReportDO;
import com.scu.lcw.blog.mapper.ReportMapper;
import com.scu.lcw.blog.pojo.request.ReportRequest;
import com.scu.lcw.blog.pojo.vo.ReportVO;
import com.scu.lcw.blog.service.ReportService;
import com.scu.lcw.blog.util.LocalDateUtils;
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
 * @date: 2021/4/20 10:26
 */
@Slf4j
@Component
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private PageUtils<ReportDO> pageUtils;

    @Resource
    private LocalDateUtils localDateUtils;

    @Override
    public Result findAllReport(ReportRequest reportRequest) {
        List<ReportDO> reportList = reportMapper.selectList(new QueryWrapper<>())
                .stream()
                .sorted(Comparator.comparing(ReportDO::getCreateTime).reversed())
                .map(reportDO -> reportDO.setDate(localDateUtils.parseCreateTime(reportDO.getCreateTime()))
                        .setDateTime(localDateUtils.dateTimeHHmmss(reportDO.getCreateTime())))
                .collect(Collectors.toList());
        return Result.data(
                new ReportVO()
                        .setReportList(pageUtils.listPagination(
                                reportList,
                                reportRequest.getCurrentPage(),
                                reportRequest.getPageSize()
                        ))
                        .setTotal(reportList.size())
        );
    }
}
