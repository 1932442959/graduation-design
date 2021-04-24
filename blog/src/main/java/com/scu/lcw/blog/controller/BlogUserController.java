package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.LoginMessageRequest;
import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.blog.service.BlogUserService;
import com.scu.lcw.blog.util.AntiBrushUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 12000, allowCredentials = "true")
@RequestMapping("/bloguser")
@Slf4j
public class BlogUserController extends BaseController {

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private AntiBrushUtils antiBrushUtils;

    @PostMapping("/login")
    public Result login(LoginRequest loginRequest) {
        log.info("loginRequest: " + loginRequest.toString());
        return blogUserService.login(loginRequest);
    }

    @RequestMapping("/logout")
    public void logout(LoginMessageRequest loginMessageRequest) {
        this.removeBlogUserMessage(loginMessageRequest.getBlogUserLoginFlag());
    }

    @PostMapping("/register")
    public Result register(RegisterRequest registerRequest) {
        log.info("registerRequest: " + registerRequest.toString());
        if (antiBrushUtils.registerAntiBrush(registerRequest)) {
            return blogUserService.register(registerRequest);
        }
        return Result.fail(RspEnum.error_so_quick);
    }

    @PostMapping("/emailvalidate")
    public Result getEmailValidateStr(RegisterRequest registerRequest) {
        log.info("registerRequest: " + registerRequest.toString());
        return blogUserService.getEmailValidateStr(registerRequest);
    }

    @RequestMapping("/getcurrent")
    public Result getCurrentBlogUser(LoginMessageRequest loginMessageRequest) {
        return Result.data(this.getBlogUserMessage(loginMessageRequest.getBlogUserLoginFlag()));
    }
}
