package com.scu.lcw.blog.service;

import com.scu.lcw.blog.entity.CommentDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.dto.CommentDTO;
import com.scu.lcw.blog.pojo.request.CommentRequest;
import com.scu.lcw.common.response.Result;

import java.util.List;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 15:41
 */
public interface CommentService {

    List<CommentDTO> findDailyComment(DailyDO dailyDO);

    List<CommentDTO> findArticleComment(Long articleId);

    Result addDailyComment(CommentRequest commentRequest);

    Result likeComment(CommentDO commentDO, String blogUserLoginFlag);

    Result dislikeComment(CommentDO commentDO, String blogUserLoginFlag);

    Result getLikeCommentList(String blogUserLoginFlag);

    Result getDislikeCommentList(String blogUserLoginFlag);

}
