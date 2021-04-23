package com.scu.lcw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.controller.BaseController;
import com.scu.lcw.blog.entity.BlogUserDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.mapper.BlogUserMapper;
import com.scu.lcw.blog.mapper.DailyMapper;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.blog.pojo.vo.DailyVO;
import com.scu.lcw.blog.service.CommentService;
import com.scu.lcw.blog.service.DailyService;
import com.scu.lcw.blog.util.LocalDateUtils;
import com.scu.lcw.blog.util.PageUtils;
import com.scu.lcw.common.response.Result;
import com.scu.lcw.common.response.RspEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:51
 */
@Slf4j
@Component
public class DailyServiceImpl extends BaseController implements DailyService {

    @Resource
    private PageUtils pageUtils;

    @Resource
    private DailyMapper dailyMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private LocalDateUtils localDateUtils;

    @Override
    public Result findAllDaily(DailyRequest dailyRequest) {
        List<DailyBO> allDaily = findDailyPage(dailyRequest)
                .stream()
                .map(DailyDO::buildDailyVO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), dailyList ->
                                dailyList.stream()
                                        .map(dailyBO -> dailyBO
                                                .setCommentList(
                                                        commentService.findDailyComment(dailyBO.getDailyDO())
                                                )
                                        )

                        )
                )
                .map(this::computeCommentLength)
                .collect(Collectors.toList());

        return Result.data(new DailyVO()
                .setDailyList(allDaily)
                .setTotal(getTotalDaily()));
    }

    @Transactional
    @Override
    public synchronized Result dislikeDaily(DailyDO dailyDO, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeDaily = blogUserMessage.getUserLikeDaily();
        String userDislikeDaily = blogUserMessage.getUserDislikeDaily();
        if (StringUtils.isEmpty(userDislikeDaily)) {
            blogUserMapper.update(
                    blogUserMessage.setUserDislikeDaily(String.valueOf(dailyDO.getDailyId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            dailyMapper.update(dailyDO.setDailyDislike(dailyDO.getDailyDislike() + 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
            decreLike(dailyDO, userLikeDaily, blogUserMessage);
            return Result.ok();
        }
        increDislike(dailyDO, userDislikeDaily, blogUserMessage);
        decreLike(dailyDO, userLikeDaily, blogUserMessage);
        return Result.ok();
    }

    @Override
    public Result getLikeDailyList(HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        String likeDaily = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserLikeDaily();
        if (StringUtils.isEmpty(likeDaily)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(likeDaily.split(",")));
    }

    @Override
    public Result getDislikeDailyList(HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        String dislikeDaily = blogUserMapper.selectList(new QueryWrapper<BlogUserDO>()
                .eq("user_id", blogUserMessage.getUserId()))
                .get(0)
                .getUserDislikeDaily();
        if (StringUtils.isEmpty(dislikeDaily)) {
            return Result.data(Collections.emptyList());
        }
        return Result.data(Arrays.asList(dislikeDaily.split(",")));
    }

    @Transactional
    @Override
    public synchronized Result likeDaily(DailyDO dailyDO, HttpServletRequest request) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(request);
        if (blogUserMessage == null) {
            return Result.fail(RspEnum.error_not_login);
        }
        String userLikeDaily = blogUserMessage.getUserLikeDaily();
        String userDislikeDaily = blogUserMessage.getUserDislikeDaily();
        if (StringUtils.isEmpty(userLikeDaily)) {
            blogUserMapper.update(
                    blogUserMessage.setUserLikeDaily(String.valueOf(dailyDO.getDailyId()))
                    , new QueryWrapper<BlogUserDO>()
                            .eq("user_id", blogUserMessage.getUserId())
            );
            dailyMapper.update(dailyDO.setDailyLike(dailyDO.getDailyLike() + 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
            decreDislike(dailyDO, userDislikeDaily, blogUserMessage);
            return Result.ok();
        }
        increLike(dailyDO, userLikeDaily, blogUserMessage);
        decreDislike(dailyDO, userDislikeDaily, blogUserMessage);
        return Result.ok();
    }

    private void increDislike(DailyDO dailyDO, String userDislikeDaily, BlogUserDO blogUser) {
        List<String> dailyDislike = Arrays.asList(userDislikeDaily.split(","));
        if (dailyDislike.contains(String.valueOf(dailyDO.getDailyId()))) {
            dailyMapper.update(dailyDO.setDailyDislike(dailyDO.getDailyDislike() - 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
            blogUserMapper.update(blogUser.setUserDislikeDaily(dailyDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(dailyDO.getDailyId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userDislikeDaily = userDislikeDaily + "," + dailyDO.getDailyId();
        blogUserMapper.update(blogUser.setUserDislikeDaily(userDislikeDaily), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        dailyMapper.update(dailyDO.setDailyDislike(dailyDO.getDailyDislike() + 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
        return;
    }

    private void increLike(DailyDO dailyDO, String userLikeDaily, BlogUserDO blogUser) {
        List<String> dailyLike = Arrays.asList(userLikeDaily.split(","));
        if (dailyLike.contains(String.valueOf(dailyDO.getDailyId()))) {
            dailyMapper.update(dailyDO.setDailyLike(dailyDO.getDailyLike() - 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
            blogUserMapper.update(blogUser.setUserLikeDaily(dailyLike.stream()
                            .filter(like -> !like.equals(String.valueOf(dailyDO.getDailyId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            return;
        }
        userLikeDaily = userLikeDaily + "," + dailyDO.getDailyId();
        blogUserMapper.update(blogUser.setUserLikeDaily(userLikeDaily), new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
        dailyMapper.update(dailyDO.setDailyLike(dailyDO.getDailyLike() + 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
        return;
    }

    private void decreDislike(DailyDO dailyDO, String userDislikeDaily, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userDislikeDaily)) {
            return;
        }
        List<String> dailyDislike = Arrays.asList(userDislikeDaily.split(","));
        if (dailyDislike.contains(String.valueOf(dailyDO.getDailyId()))) {
            blogUserMapper.update(blogUser.setUserDislikeDaily(dailyDislike.stream()
                            .filter(dislike -> !dislike.equals(String.valueOf(dailyDO.getDailyId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            dailyMapper.update(dailyDO.setDailyDislike(dailyDO.getDailyDislike() - 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
        }
        return;
    }

    private void decreLike(DailyDO dailyDO, String userLikeDaily, BlogUserDO blogUser) {
        if (StringUtils.isEmpty(userLikeDaily)) {
            return;
        }
        List<String> dailyLike = Arrays.asList(userLikeDaily.split(","));
        if (dailyLike.contains(String.valueOf(dailyDO.getDailyId()))) {
            blogUserMapper.update(blogUser.setUserLikeDaily(dailyLike.stream()
                            .filter(like -> !like.equals(String.valueOf(dailyDO.getDailyId())))
                            .collect(Collectors.joining(",")))
                    , new QueryWrapper<BlogUserDO>().eq("user_id", blogUser.getUserId()));
            dailyMapper.update(dailyDO.setDailyLike(dailyDO.getDailyLike() - 1L), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId()));
        }
        return;
    }

    private Integer getTotalDaily() {
        return dailyMapper.selectCount(new QueryWrapper<>());
    }

    private DailyBO computeCommentLength(DailyBO dailyBO) {
        dailyBO.getDailyDO().setCommentLength(dailyBO.getCommentList().size());
        return dailyBO;
    }

    private List<DailyDO> findDailyPage(DailyRequest dailyRequest) {
        return pageUtils.listPagination(
                dailyMapper.selectList(new QueryWrapper<>())
                        .stream()
                        .sorted(Comparator.comparing(DailyDO::getCreateTime).reversed())
                        .map(dailyDO -> dailyDO
                                .setDateTime(localDateUtils.parseCreateTime(dailyDO.getCreateTime()))
                                .setDate(localDateUtils.dateTimeHHmmss(dailyDO.getCreateTime()))
                        )
                        .collect(Collectors.toList()),
                dailyRequest.getCurrentPage(),
                dailyRequest.getPageSize()
        );
    }

}
