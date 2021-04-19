package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.service.LabelService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/label")
public class LabelController {

    @Resource
    private LabelService labelService;

    @RequestMapping("/getlist")
    public Result getLabelList() {
        return labelService.getLabelList();
    }

    @RequestMapping("/getAllList")
    public Result getAllList() {
        return labelService.getAllLabelList();
    }
}
