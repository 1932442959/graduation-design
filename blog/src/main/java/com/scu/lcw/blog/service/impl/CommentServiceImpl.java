package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.mapper.CommentMapper;
import com.scu.lcw.blog.pojo.dto.CommentDTO;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.blog.pojo.request.CommentTypeEnum;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 15:41
 */
@Component
@Slf4j
public class CommentServiceImpl extends BaseController implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<CommentDTO> findDailyComment(DailyDO dailyDO) {
        //获取父节点
        List<CommentDTO> dailyComments = convertParent(findAllDailyComment(dailyDO))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .collect(Collectors.toList());

        //拼装子节点
        dailyComments.stream()
                .forEach(this::convertChild);

        return dailyComments;
    }

    @Override
    public List<CommentDTO> findArticleComment(Long articleId) {
        //获取父节点
        List<CommentDTO> articleComments = convertParent(findAllArticleComment(articleId))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .collect(Collectors.toList());

        //拼装子节点
        articleComments.stream()
                .forEach(this::convertChild);

        return articleComments;
    }

    @Override
    public Result addDailyComment(CommentRequest commentRequest, HttpServletRequest request) {
        int insertResult = commentMapper.insert(CommentDO.buildCommentDO(commentRequest, this.getBlogUserMessage(request)));
        return Result.data(insertResult);
    }

    private List<CommentDO> findAllDailyComment(DailyDO dailyDO) {
        return commentMapper.selectList(new QueryWrapper<CommentDO>()
                .eq("refrence_id", dailyDO.getDailyId())
                .eq("comment_type", CommentTypeEnum.DAILY));
    }

    private List<CommentDO> findAllArticleComment(Long articleId) {
        return commentMapper.selectList(new QueryWrapper<CommentDO>()
                .eq("refrence_id", articleId)
                .eq("comment_type", CommentTypeEnum.ARTICLE));
    }

    private List<CommentDO> convertParent(List<CommentDO> commentList) {
        return commentList.stream()
                .filter(commentDO -> commentDO.getParentId().equals(0L))
                .sorted(Comparator.comparing(CommentDO::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    private void convertChild(CommentDTO parentComment) {
        List<CommentDTO> childList = commentMapper.selectList(
                new QueryWrapper<CommentDO>()
                        .eq("parent_id", parentComment.getCommentId()))
                .stream()
                .map(CommentDTO::buildCommentDTO)
                .sorted(Comparator.comparing(CommentDTO::getCreateTime).reversed())
                .collect(Collectors.toList());

        if (childList.size() == 0) {
            return;
        }

        parentComment.setChildList(childList);
    }
}
