package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.ReportRequest;
import com.scu.lcw.blog.service.ReportService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:26
 */
@RestController
@Slf4j
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @RequestMapping("/getlist")
    public Result findReportList(ReportRequest reportRequest) {
        return reportService.findAllReport(reportRequest);
    }
}
