package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.MessageDO;
import com.scu.lcw.blog.mapper.MessageMapper;
import com.scu.lcw.blog.pojo.request.MessageRequest;
import com.scu.lcw.blog.pojo.vo.MessageVO;
import com.scu.lcw.blog.service.MessageService;
import com.scu.lcw.blog.util.LocalDateUtils;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
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

    @Resource
    private LocalDateUtils localDateUtils;

    @Override
    public Result findAllMessage(MessageRequest messageRequest) {
        List<MessageDO> allMessage = messageMapper.selectList(new QueryWrapper<>())
                .stream()
                .sorted(Comparator.comparing(MessageDO::getCreateTime).reversed())
                .map(messageDO -> messageDO
                        .setDate(localDateUtils.parseCreateTime(messageDO.getCreateTime()))
                        .setDateTime(localDateUtils.dateTimeHHmmss(messageDO.getCreateTime())))
                .collect(Collectors.toList());

        return Result.data(new MessageVO()
                .setMessageList(pageUtils.listPagination(allMessage, messageRequest.getCurrentPage(), messageRequest.getPageSize()))
                .setTotal(allMessage.size())
        );
    }

    @Transactional
    @Override
    public Result addMessage(MessageRequest messageRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(messageRequest.getBlogUserLoginFlag());
        Integer addResult = messageMapper.insert(MessageDO.buildMessageDO(blogUserMessage, messageRequest.getMessageContent()));
        return Result.data(addResult);
    }
}
