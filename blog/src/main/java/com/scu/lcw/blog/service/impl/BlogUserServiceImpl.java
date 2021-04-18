package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.blog.service.BlogUserService;
import com.scu.lcw.blog.util.MailUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Component
public class BlogUserServiceImpl extends BaseController implements BlogUserService {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private MailUtils mailUtils;

    @Override
    public Result login(LoginRequest loginRequest, HttpServletRequest request) {
        log.info("loginRequest: " + loginRequest.toString());

        Optional<BlogUserDO> user = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_name", loginRequest.getLoginName()))
                .stream()
                .findFirst();

        if (!user.isPresent()) {
            return Result.fail(RspEnum.error_no_user);
        }

        if (user.get().getEnabled().equals("N")) {
            return Result.fail(RspEnum.error_status);
        }

        if (user.get().getUserPassword().equals(loginRequest.getLoginPassword())) {
            this.saveBlogUserMessage(request, user.get());
            return Result.ok();
        }

        return Result.fail(RspEnum.error_password);
    }

    @Override
    public Result getEmailValidateStr(RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("registerRequest: " + registerRequest.toString());

        boolean sameUser = blogUserMapper.selectList(new QueryWrapper<>())
                .stream()
                .anyMatch(blogUserDO -> blogUserDO.getUserName().equals(registerRequest.getRegisterName()));

        if (sameUser) {
            return Result.fail(RspEnum.error_same_user);
        }

        if (validateFormCondition(registerRequest)) {
            String validateStr = mailUtils.sendMail(registerRequest.getEmail());
            request.getSession().setAttribute(getRegisterBlogUserSessionKey(registerRequest, request), validateStr);
            //打印该SessionId下的注册用户名对应验证码SessionKey
            log.info(getRegisterBlogUserSessionKey(registerRequest, request), validateStr);
            return Result.data(validateStr);
        }

        return Result.fail(RspEnum.error_form);
    }

    @Override
    @Transactional
    public synchronized Result register(RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("registerRequest: " + registerRequest.toString());

        boolean sameUser = blogUserMapper.selectList(new QueryWrapper<>())
                .stream()
                .anyMatch(blogUserDO -> blogUserDO.getUserName().equals(registerRequest.getRegisterName()));

        if (sameUser) {
            return Result.fail(RspEnum.error_same_user);
        }

        if (validateFormRegisterCondition(registerRequest)) {
            String validateStr = (String) request.getSession().getAttribute(getRegisterBlogUserSessionKey(registerRequest, request));
            return doRegisterBlogUser(validateStr, registerRequest);
        }

        return Result.fail(RspEnum.error_form);
    }

    private String getRegisterBlogUserSessionKey(RegisterRequest registerRequest, HttpServletRequest request) {
        return "registerBlogUser " + registerRequest.getRegisterName() + " sessionId " + request.getSession().getId();
    }

    private boolean validateFormCondition(RegisterRequest registerRequest) {
        boolean flag = StringUtils.isNotEmpty(registerRequest.getRegisterName())
                && StringUtils.isNotEmpty(registerRequest.getRegisterPassword())
                && StringUtils.isNotEmpty(registerRequest.getRepeatPassword())
                && StringUtils.isNotEmpty(registerRequest.getEmail());

        return flag;
    }

    private boolean validateFormRegisterCondition(RegisterRequest registerRequest) {
        boolean flag = StringUtils.isNotEmpty(registerRequest.getRegisterName())
                && StringUtils.isNotEmpty(registerRequest.getRegisterPassword())
                && StringUtils.isNotEmpty(registerRequest.getRepeatPassword())
                && StringUtils.isNotEmpty(registerRequest.getEmail())
                && StringUtils.isNotEmpty(registerRequest.getValidateEmail());

        return flag;
    }

    private Result doRegisterBlogUser(String validateStr, RegisterRequest registerRequest) {
        if (validateStr.equals(registerRequest.getValidateEmail())) {
            blogUserMapper.insert(BlogUserDO.buildBlogUserDO(registerRequest));
            return Result.ok();
        }

        return Result.fail(RspEnum.error_validate_email);
    }

}