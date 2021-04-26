package com.scu.lcw.backsystem.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.backsystem.Service.ProgressService;
import com.scu.lcw.backsystem.controller.UserController;
import com.scu.lcw.backsystem.entity.BlogUserDO;
import com.scu.lcw.backsystem.entity.DailyDO;
import com.scu.lcw.backsystem.entity.ProgressDO;
import com.scu.lcw.backsystem.mapper.ProgressMapper;
import com.scu.lcw.backsystem.pojo.request.ProgressRequest;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    @Resource
    private ProgressMapper progressMapper;

    @Resource
    private UserController userController;

    @Override
    public Result delete(ProgressRequest progressRequest) {
        return Result.data(progressMapper.delete(new QueryWrapper<ProgressDO>().eq("progress_id", progressRequest.getProgressId())));
    }

    @Override
    public Result addProgress(ProgressRequest progressRequest) {
        BlogUserDO manager = userController.findManager();
        return Result.data(progressMapper.insert(
                new ProgressDO()
                        .setProgressContent(progressRequest.getProgressContent())
                        .setProgressNetname(manager.getUserNetname())
                        .setProgressAvator(manager.getUserAvator())
                        .setCreateTime(LocalDateTime.now())));
    }

    @Override
    public Result editProgress(ProgressRequest progressRequest) {
        ProgressDO progress = progressMapper.selectList(new QueryWrapper<ProgressDO>().eq("progress_id", progressRequest.getProgressId())).get(0);
        return Result.data(progressMapper.update(progress.setProgressContent(progressRequest.getProgressContent()), new QueryWrapper<ProgressDO>().eq("progress_id", progressRequest.getProgressId())));
    }
}
