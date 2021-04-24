package com.scu.lcw.blog.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
        redisTemplate.opsForValue().set(blogUserLoginFlag, newBlogUser);
    }

    protected String saveBlogUserMessage(BlogUserDO blogUserDO) {
        String blogUserLoginFlag = getBlogUserLoginFlag();
        redisTemplate.opsForValue().set(blogUserLoginFlag, blogUserDO);
        return blogUserLoginFlag;
    }

    protected BlogUserDO getBlogUserMessage(String blogUserLoginFlag) {
        return (BlogUserDO) redisTemplate.opsForValue().get(blogUserLoginFlag);
    }

    protected void removeBlogUserMessage(String blogUserLoginFlag) {
        redisTemplate.delete(blogUserLoginFlag);
    }

    private String getBlogUserLoginFlag() {
        return RandomUtil.randomUUID();
    }
}
