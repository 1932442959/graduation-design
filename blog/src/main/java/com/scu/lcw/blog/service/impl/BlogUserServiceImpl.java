package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.blog.pojo.request.LoginRequest;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import com.scu.lcw.blog.service.BlogUserService;
import com.scu.lcw.blog.util.MailUtils;
import com.scu.lcw.blog.util.RequestUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BlogUserServiceImpl extends BaseController implements BlogUserService {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private MailUtils mailUtils;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RequestUtils requestUtils;

    @Override
    public Result login(LoginRequest loginRequest) {
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
            String blogUserLoginFlag = this.saveBlogUserMessage(user.get());
            return Result.data(blogUserLoginFlag);
        }

        return Result.fail(RspEnum.error_password);
    }

    @Override
    public Result getEmailValidateStr(RegisterRequest registerRequest) {
        log.info("registerRequest: " + registerRequest.toString());
//        String ipAddress = requestUtils.getIpAddress(request);
//        log.info("request-ipAddress: " + ipAddress);

        String validateStrAntiBrushKey = "validateStrAntiBrushKeyEmail " + registerRequest.getEmail();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(validateStrAntiBrushKey) != null) {
            return Result.fail(RspEnum.error_time_not_enough);
        }

        boolean sameUser = blogUserMapper.selectList(new QueryWrapper<>())
                .stream()
                .anyMatch(blogUserDO -> blogUserDO.getUserName().equals(registerRequest.getRegisterName()));

        if (sameUser) {
            return Result.fail(RspEnum.error_same_user);
        }

        if (validateFormCondition(registerRequest)) {
            String validateStr = mailUtils.sendMail(registerRequest.getEmail());
            //60s?????? ??????
            valueOperations.set(validateStrAntiBrushKey, 1, 60, TimeUnit.SECONDS);
            //???????????????
            valueOperations.set(getRegisterBlogUserSessionKey(registerRequest), validateStr, 300L, TimeUnit.MINUTES);
            //???????????????????????????????????????SessionKey
            log.info(getRegisterBlogUserSessionKey(registerRequest), validateStr);
            return Result.data(validateStr);
        }

        return Result.fail(RspEnum.error_form);
    }

    @Override
    @Transactional
    public synchronized Result register(RegisterRequest registerRequest) {
        log.info("registerRequest: " + registerRequest.toString());

        boolean sameUser = blogUserMapper.selectList(new QueryWrapper<>())
                .stream()
                .anyMatch(blogUserDO -> blogUserDO.getUserName().equals(registerRequest.getRegisterName()));

        if (sameUser) {
            return Result.fail(RspEnum.error_same_user);
        }

        if (validateFormRegisterCondition(registerRequest)) {
            return doRegisterBlogUser(registerRequest);
        }

        return Result.fail(RspEnum.error_form);
    }

    private String getRegisterBlogUserSessionKey(RegisterRequest registerRequest) {
        return "registerValidateKeyEmail" + registerRequest.getEmail();
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

    private Result doRegisterBlogUser(RegisterRequest registerRequest) {
        //?????????
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String validateStr = (String) valueOperations.get(getRegisterBlogUserSessionKey(registerRequest));
        if (validateStr.equals(registerRequest.getValidateEmail())) {
            //??????????????????
            if (valueOperations.get(getRegisterBlogUserSessionKey(registerRequest)) == null) {
                return Result.fail(RspEnum.error_validate_time_out);
            }
            blogUserMapper.insert(BlogUserDO.buildBlogUserDO(registerRequest));
            return Result.ok();
        }

        return Result.fail(RspEnum.error_validate_email);
    }

}
