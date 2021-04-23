package com.scu.lcw.blog.util;

import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AntiBrushUtils extends BaseController {

    @Resource
    private RedisTemplate redisTemplate;

    public Boolean registerAntiBrush(HttpServletRequest request) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get("register-anti-brush" + request.getSession().getId()) != null) {
            return false;
        }
        valueOperations.set("register-anti-brush" + request.getSession().getId(), "register-anti-brush", 500, TimeUnit.MILLISECONDS);
        return true;
    }

    public Boolean buttonAntiBrush(HttpServletRequest request) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String userName = this.getBlogUserMessage(request).getUserName();
        if (valueOperations.get("button-anti-brush" + userName) != null) {
            return false;
        }
        valueOperations.set("button-anti-brush" + userName, "button-anti-brush", 500, TimeUnit.MILLISECONDS);
        return true;
    }
}
