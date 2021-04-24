package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.request.DailyLikeRequest;
import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.blog.pojo.request.LoginMessageRequest;
import com.scu.lcw.blog.service.DailyService;
import com.scu.lcw.blog.util.AntiBrushUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
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
public class DailyController extends BaseController {

    @Resource
    private DailyService dailyService;

    @Resource
    private AntiBrushUtils antiBrushUtils;

    @RequestMapping("/getlist")
    public Result getDailyList(DailyRequest dailyRequest) {
        return dailyService.findAllDaily(dailyRequest);
    }

    @PostMapping("/likedaily")
    public Result likeDaily(DailyLikeRequest dailyLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(dailyLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(dailyLikeRequest.getBlogUserLoginFlag())) {
            Result result = dailyService.likeDaily(DailyDO.buildDailyDO(dailyLikeRequest), dailyLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(dailyLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @PostMapping("/dislikedaily")
    public Result dislikeDaily(DailyLikeRequest dailyLikeRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(dailyLikeRequest.getBlogUserLoginFlag());
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        if (antiBrushUtils.buttonAntiBrush(dailyLikeRequest.getBlogUserLoginFlag())) {
            Result result = dailyService.dislikeDaily(DailyDO.buildDailyDO(dailyLikeRequest), dailyLikeRequest.getBlogUserLoginFlag());
            this.updateBlogUserMessage(dailyLikeRequest.getBlogUserLoginFlag());
            return result;
        }
        return Result.ok();
    }

    @RequestMapping("/likedailylist")
    public Result getLikeDailyList(LoginMessageRequest loginMessageRequest) {
        return dailyService.getLikeDailyList(loginMessageRequest.getBlogUserLoginFlag());
    }

    @RequestMapping("/dislikedailylist")
    public Result getDislikeDailyList(LoginMessageRequest loginMessageRequest) {
        return dailyService.getDislikeDailyList(loginMessageRequest.getBlogUserLoginFlag());
    }
}
