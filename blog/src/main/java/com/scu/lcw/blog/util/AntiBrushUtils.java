package com.scu.lcw.blog.util;

import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
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

    public Boolean registerAntiBrush(RegisterRequest registerRequest) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get("register-anti-brush" + valueOperations.get(registerAntiBrushKey(registerRequest))) != null) {
            return false;
        }
        valueOperations.set(registerAntiBrushKey(registerRequest), "register-anti-brush", 500, TimeUnit.MILLISECONDS);
        return true;
    }

    private String registerAntiBrushKey(RegisterRequest registerRequest) {
        return "register-anti-brush" + registerRequest.getRegisterName() + registerRequest.getRegisterPassword() + registerRequest.getEmail() + registerRequest.getValidateEmail();
    }

    public Boolean buttonAntiBrush(String blogUserLoginFlag) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String userName = this.getBlogUserMessage(blogUserLoginFlag).getUserName();
        if (valueOperations.get("button-anti-brush" + userName) != null) {
            return false;
        }
        valueOperations.set("button-anti-brush" + userName, "button-anti-brush", 500, TimeUnit.MILLISECONDS);
        return true;
    }
}
