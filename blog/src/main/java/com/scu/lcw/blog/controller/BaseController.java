package com.scu.lcw.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class BaseController {

    @Resource
    private BlogUserMapper blogUserMapper;

    protected void updateBlogUserMessage(HttpServletRequest request) {
        BlogUserDO blogUser = getBlogUserMessage(request);
        BlogUserDO newBlogUser = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId())).get(0);
        saveBlogUserMessage(request, newBlogUser);
    }

    protected void saveBlogUserMessage(HttpServletRequest request, BlogUserDO blogUserDO) {
        request.getSession().setAttribute("currentBlogUser", blogUserDO);
    }

    protected BlogUserDO getBlogUserMessage(HttpServletRequest request) {
        BlogUserDO currentBlogUser = (BlogUserDO) request.getSession().getAttribute("currentBlogUser");
        return currentBlogUser;
    }

    protected void removeBlogUserMessage(HttpServletRequest request) {
        request.getSession().removeAttribute("currentBlogUser");
    }
}
