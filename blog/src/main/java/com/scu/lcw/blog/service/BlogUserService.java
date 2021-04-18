package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.common.response.Result;

import javax.servlet.http.HttpServletRequest;

public interface BlogUserService {

    Result login(LoginRequest loginRequest, HttpServletRequest request);

    Result getEmailValidateStr(RegisterRequest registerRequest, HttpServletRequest request);

    Result register(RegisterRequest registerRequest, HttpServletRequest request);
}
