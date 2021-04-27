package com.scu.lcw.blog.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.SystemRuntimeDO;
import com.scu.lcw.blog.entity.SystemVisitDO;
import com.scu.lcw.blog.mapper.SystemRuntimeMapper;
import com.scu.lcw.blog.mapper.SystemVisitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ScheduleUtils {

    private final static String RUN_TIME_KEY = "system_run_time";

    private final static String HOME_VISIT_TODAY = "home_visit_today";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SystemRuntimeMapper systemRuntimeMapper;

    @Resource
    private SystemVisitMapper systemVisitMapper;

    @Scheduled(cron = "*/1 * * * * *")
    public void recordRunTime() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long time = (Long) valueOperations.get(RUN_TIME_KEY);
        if (time == null) {
            valueOperations.set(RUN_TIME_KEY, 1L);
            return;
        }
        valueOperations.set(RUN_TIME_KEY, time + 1L);
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void saveRunTime() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long time = (Long) valueOperations.get(RUN_TIME_KEY);
        Long runTime = systemRuntimeMapper.selectList(new QueryWrapper<>()).get(0).getRunTime();
        if (time < runTime) {
            throw new RuntimeException("系统异常,时间同步出错！");
        }
        systemRuntimeMapper.update(new SystemRuntimeDO()
                        .setRunTime(time)
                , new QueryWrapper<SystemRuntimeDO>().eq("id", 1));
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void saveHomeVisit() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long visitNum = (Long) valueOperations.get(HOME_VISIT_TODAY);
        systemVisitMapper.insert(new SystemVisitDO()
                .setHomeVisit(visitNum)
                .setCreateTime(LocalDateTime.now()));
        valueOperations.set(HOME_VISIT_TODAY, 0L);
    }

}
