package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.MessageDO;
import com.scu.lcw.blog.mapper.MessageMapper;
import com.scu.lcw.blog.pojo.request.MessageRequest;
import com.scu.lcw.blog.service.MessageService;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:21
 */
@Component
@Slf4j
public class MessageServiceImpl extends BaseController implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private PageUtils<MessageDO> pageUtils;

    @Override
    public Result findAllMessage(MessageRequest messageRequest) {
        return Result.data(
                pageUtils.listPagination(
                        messageMapper.selectList(new QueryWrapper<>())
                                .stream()
                                .sorted(Comparator.comparing(MessageDO::getCreateTime).reversed())
                                .collect(Collectors.toList()),
                        messageRequest.getCurrentPage(),
                        messageRequest.getPageSize()
                )
        );
    }

    @Override
    public Result addMessage(MessageRequest messageRequest, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        Integer addResult = messageMapper.insert(MessageDO.buildMessageDO(blogUserMessage, messageRequest.getMessageContent()));
        return Result.data(addResult);
    }
}
