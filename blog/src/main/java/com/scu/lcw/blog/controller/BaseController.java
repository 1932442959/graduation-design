package com.scu.lcw.blog.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class BaseController {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private RedisTemplate redisTemplate;

    protected void updateBlogUserMessage(String blogUserLoginFlag) {
        BlogUserDO blogUser = getBlogUserMessage(blogUserLoginFlag);
        BlogUserDO newBlogUser = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId())).get(0);
        redisTemplate.opsForValue().set(blogUserLoginFlag, newBlogUser, 30L, TimeUnit.DAYS);
    }

    protected String saveBlogUserMessage(BlogUserDO blogUserDO) {
        String blogUserLoginFlag = getBlogUserLoginFlag();
        redisTemplate.opsForValue().set(blogUserLoginFlag, blogUserDO, 30L, TimeUnit.DAYS);
        return blogUserLoginFlag;
    }

    protected BlogUserDO getBlogUserMessage(String blogUserLoginFlag) {
        if (StringUtils.isEmpty(blogUserLoginFlag)) {
            return null;
        }
        return (BlogUserDO) redisTemplate.opsForValue().get(blogUserLoginFlag);
    }

    protected void removeBlogUserMessage(String blogUserLoginFlag) {
        redisTemplate.delete(blogUserLoginFlag);
    }

    private String getBlogUserLoginFlag() {
        return RandomUtil.randomUUID();
    }
}
