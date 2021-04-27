package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.LabelRequest;
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

    @RequestMapping("/addparent")
    public Result addparent(LabelRequest labelRequest) {
        return labelService.addParent(labelRequest);
    }

    @RequestMapping("/addchild")
    public Result addchild(LabelRequest labelRequest) {
        return labelService.addChild(labelRequest);
    }

    @RequestMapping("/update")
    public Result update(LabelRequest labelRequest) {
        return labelService.update(labelRequest);
    }

    @RequestMapping("/delete")
    public Result delete(LabelRequest labelRequest) {
        return labelService.delete(labelRequest);
    }

}
