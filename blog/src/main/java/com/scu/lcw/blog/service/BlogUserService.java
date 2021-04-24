package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.common.response.Result;

import javax.servlet.http.HttpServletRequest;

public interface BlogUserService {

    Result login(LoginRequest loginRequest);

    Result getEmailValidateStr(RegisterRequest registerRequest);

    Result register(RegisterRequest registerRequest);
}
