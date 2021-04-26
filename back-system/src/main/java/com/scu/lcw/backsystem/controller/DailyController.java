package com.scu.lcw.backsystem.controller;

import com.scu.lcw.backsystem.Service.DailyService;
import com.scu.lcw.backsystem.pojo.request.DailyRequest;
import com.scu.lcw.common.response.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dailymng")
public class DailyController {

    @Resource
    private DailyService dailyService;

    @RequestMapping("/delete")
    public Result delete(DailyRequest dailyRequest) {
        return dailyService.delete(dailyRequest);
    }

    @RequestMapping("/add")
    public Result addDaily(DailyRequest dailyRequest) {
        return dailyService.addDaily(dailyRequest);
    }
    @RequestMapping("/edit")
    public Result editDaily(DailyRequest dailyRequest) {
        return dailyService.editDaily(dailyRequest);
    }

}
