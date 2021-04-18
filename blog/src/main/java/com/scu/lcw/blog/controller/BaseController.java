package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.entity.BlogUserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class BaseController {

    protected void saveBlogUserMessage(HttpServletRequest request, BlogUserDO blogUserDO) {
        request.getSession().setAttribute("currentBlogUser", blogUserDO);
    }

    protected BlogUserDO getBlogUserMessage(HttpServletRequest request) {
        BlogUserDO currentBlogUser = (BlogUserDO) request.getSession().getAttribute("currentBlogUser");
        return currentBlogUser;
    }
}
