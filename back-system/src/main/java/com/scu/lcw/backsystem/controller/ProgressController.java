package com.scu.lcw.backsystem.controller;

import com.scu.lcw.backsystem.Service.DailyService;
import com.scu.lcw.backsystem.Service.ProgressService;
import com.scu.lcw.backsystem.pojo.request.DailyRequest;
import com.scu.lcw.backsystem.pojo.request.ProgressRequest;
import com.scu.lcw.common.response.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/progressmng")
public class ProgressController {

    @Resource
    private ProgressService progressService;

    @RequestMapping("/delete")
    public Result delete(ProgressRequest progressRequest) {
        return progressService.delete(progressRequest);
    }

    @RequestMapping("/add")
    public Result addDaily(ProgressRequest progressRequest) {
        return progressService.addProgress(progressRequest);
    }

    @RequestMapping("/edit")
    public Result editDaily(ProgressRequest progressRequest) {
        return progressService.editProgress(progressRequest);
    }
}
