package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.blog.service.BlogUserService;
import com.scu.lcw.common.response.Result;
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

    @PostMapping("/login")
    public Result login(LoginRequest loginRequest, HttpServletRequest request) {
        log.info("loginRequest: " + loginRequest.toString());
        return blogUserService.login(loginRequest, request);
    }

    @RequestMapping("/logout")
    public void loglout(HttpServletRequest request) {
        this.removeBlogUserMessage(request);
    }

    @PostMapping("/register")
    public Result register(RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("registerRequest: " + registerRequest.toString());
        return blogUserService.register(registerRequest, request);
    }

    @PostMapping("/emailvalidate")
    public Result getEmailValidateStr(RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("registerRequest: " + registerRequest.toString());
        return blogUserService.getEmailValidateStr(registerRequest, request);
    }

    @RequestMapping("/getcurrent")
    public Result getCurrentBlogUser(HttpServletRequest request) {
        return Result.data(this.getBlogUserMessage(request));
    }
}
