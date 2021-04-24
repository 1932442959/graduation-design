package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.ProgressRequest;
import com.scu.lcw.blog.service.ProgressService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/progress")
@Slf4j
public class ProgressController {

    @Resource
    private ProgressService progressService;

    @RequestMapping("/getlist")
    public Result getProgressList(ProgressRequest progressRequest) {
        return progressService.getProgressList(progressRequest);
    }

}
