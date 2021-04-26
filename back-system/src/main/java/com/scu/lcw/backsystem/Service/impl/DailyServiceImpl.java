package com.scu.lcw.backsystem.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.backsystem.Service.DailyService;
import com.scu.lcw.backsystem.controller.UserController;
import com.scu.lcw.backsystem.entity.BlogUserDO;
import com.scu.lcw.backsystem.entity.DailyDO;
import com.scu.lcw.backsystem.mapper.DailyMapper;
import com.scu.lcw.backsystem.pojo.request.DailyRequest;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DailyServiceImpl implements DailyService {

    @Resource
    private DailyMapper dailyMapper;

    @Resource
    private UserController userController;

    @Override
    public Result delete(DailyRequest dailyRequest) {
        return Result.data(dailyMapper.delete(new QueryWrapper<DailyDO>().eq("daily_id", dailyRequest.getDailyId())));
    }

    @Override
    public Result addDaily(DailyRequest dailyRequest) {
        BlogUserDO manager = userController.findManager();
        return Result.data(dailyMapper.insert(
                new DailyDO()
                        .setDailyLike(0L)
                        .setDailyDislike(0L)
                        .setDailyContent(dailyRequest.getDailyContent())
                        .setDailyUsername(manager.getUserName())
                        .setDailyNetname(manager.getUserNetname())
                        .setDailyAvator(manager.getUserAvator())
                        .setCreateTime(LocalDateTime.now())));
    }

    @Override
    public Result editDaily(DailyRequest dailyRequest) {
        DailyDO daily = dailyMapper.selectList(new QueryWrapper<DailyDO>().eq("daily_id", dailyRequest.getDailyId())).get(0);
        return Result.data(dailyMapper.update(daily.setDailyContent(dailyRequest.getDailyContent()), new QueryWrapper<DailyDO>().eq("daily_id", dailyRequest.getDailyId())));
    }

}
