package com.scu.lcw.blog.controller;

import com.scu.lcw.blog.pojo.request.MessageRequest;
import com.scu.lcw.blog.service.MessageService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:51
 */
@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @RequestMapping("/getlist")
    public Result getMessageList(MessageRequest messageRequest) {
        return messageService.findAllMessage(messageRequest);
    }

    @RequestMapping("/addmessage")
    public Result addNewMessage(MessageRequest messageRequest, HttpServletRequest request) {
        return messageService.addMessage(messageRequest, request);
    }
}
