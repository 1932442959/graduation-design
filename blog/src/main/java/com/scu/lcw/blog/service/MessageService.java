package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.MessageRequest;
import com.scu.lcw.common.response.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:20
 */
public interface MessageService {

    Result findAllMessage(MessageRequest messageRequest);

    Result addMessage(MessageRequest messageRequest, HttpServletRequest request);
}
