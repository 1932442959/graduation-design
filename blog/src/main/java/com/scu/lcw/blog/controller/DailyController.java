package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.blog.service.DailyService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 17:32
 */
@RestController
@Slf4j
@RequestMapping("/daily")
public class DailyController {

    @Resource
    private DailyService dailyService;

    @RequestMapping("/getlist")
    public Result getDailyList(DailyRequest dailyRequest) {
        return dailyService.findAllDaily(dailyRequest);
    }
}
