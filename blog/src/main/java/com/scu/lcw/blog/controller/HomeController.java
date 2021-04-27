package com.scu.lcw.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.entity.*;
import com.scu.lcw.blog.mapper.*;
import com.scu.lcw.blog.pojo.request.ChangeAvatorRequest;
import com.scu.lcw.blog.pojo.request.HomeRequest;
import com.scu.lcw.blog.pojo.request.MovieRequest;
import com.scu.lcw.blog.pojo.vo.CardVO;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController extends BaseController {

    private final static String RUN_TIME_KEY = "system_run_time";

    private final static String HOME_VISIT_TODAY = "home_visit_today";

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private DailyMapper dailyMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MovieMapper movieMapper;

    @Resource
    private BlogUserMapper blogUserMapper;

    @RequestMapping("/changeavator")
    @Transactional
    public Result changeAvator(ChangeAvatorRequest changeAvatorRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        int resultId = blogUserMapper.update(blogUserMessage.setUserAvator(changeAvatorRequest.getAvator()),
                new QueryWrapper<BlogUserDO>().eq("user_id", blogUserMessage.getUserId()));
        this.updateBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        blogUserMessage = this.getBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        this.changeTogether(blogUserMessage);
        return Result.data(resultId);
    }

    @RequestMapping("/changenetname")
    @Transactional
    public Result changeNetname(ChangeAvatorRequest changeAvatorRequest) {
        BlogUserDO blogUserMessage = this.getBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        int resultId = blogUserMapper.update(blogUserMessage.setUserNetname(changeAvatorRequest.getNetname()),
                new QueryWrapper<BlogUserDO>().eq("user_id", blogUserMessage.getUserId()));
        this.updateBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        blogUserMessage = this.getBlogUserMessage(changeAvatorRequest.getBlogUserLoginFlag());
        this.changeTogether(blogUserMessage);
        return Result.data(resultId);
    }

    @RequestMapping("/card")
    public Result cardMessage() {
        return Result.data(new CardVO()
                .setArticleNum(articleMapper.selectCount(new QueryWrapper<>()))
                .setDailyNum(dailyMapper.selectCount(new QueryWrapper<>()))
                .setCommentNum(commentMapper.selectCount(new QueryWrapper<>())));
    }

    @RequestMapping("/currenttime")
    public Result getRunTime() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return Result.data(valueOperations.get(RUN_TIME_KEY));
    }

    @RequestMapping("/addvisit")
    public void addVisit() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long visitNum = (Long) valueOperations.get(HOME_VISIT_TODAY);
        if (visitNum == null) {
            valueOperations.set(HOME_VISIT_TODAY, 1L);
            return;
        }
        valueOperations.set(HOME_VISIT_TODAY, visitNum + 1);
    }

    @RequestMapping("/movie")
    public Result findAllMovie() {
        return Result.data(movieMapper.selectList(new QueryWrapper<>()));
    }

    @RequestMapping("/findmovie")
    public Result findMovie(MovieRequest movieRequest) {
        return Result.data(movieMapper.selectList(new QueryWrapper<MovieDO>().eq("id", movieRequest.getId())).get(0));
    }

    @RequestMapping("/addmovie")
    public Result addmovie(MovieRequest movieRequest) {
        return Result.data(movieMapper.insert(new MovieDO()
                .setFirst(movieRequest.getFirst())
                .setSecond(movieRequest.getSecond())
                .setThird(movieRequest.getThird())));
    }

    @RequestMapping("/deletemovie")
    public Result deletemovie(MovieRequest movieRequest) {
        return Result.data(movieMapper.delete(new QueryWrapper<MovieDO>().eq("id", movieRequest.getId())));
    }

    @RequestMapping("/updatemovie")
    public Result updatemovie(MovieRequest movieRequest) {
        MovieDO movie = movieMapper.selectList(new QueryWrapper<MovieDO>().eq("id", movieRequest.getId())).get(0);
        return Result.data(movieMapper.update(movie
                        .setThird(movieRequest.getThird())
                        .setSecond(movieRequest.getSecond())
                        .setFirst(movieRequest.getFirst())
                , new QueryWrapper<MovieDO>().eq("id", movieRequest.getId())));
    }

    private void changeTogether(BlogUserDO blogUserDO) {
        String userName = blogUserDO.getUserName();
        List<CommentDO> comments = commentMapper.selectList(new QueryWrapper<CommentDO>().eq("comment_username", userName));
        if (comments.size() != 0) {
            comments.stream()
                    .forEach(commentDO -> commentMapper.update(commentDO.setCommentNetname(blogUserDO.getUserNetname())
                            .setCommentAvator(blogUserDO.getUserAvator()), new QueryWrapper<CommentDO>().eq("comment_id", commentDO.getCommentId())));
        }
        List<DailyDO> dailys = dailyMapper.selectList(new QueryWrapper<DailyDO>().eq("daily_username", userName));
        if (dailys.size() != 0) {
            dailys.stream()
                    .forEach(dailyDO -> dailyMapper.update(dailyDO.setDailyAvator(blogUserDO.getUserAvator())
                            .setDailyNetname(blogUserDO.getUserNetname()), new QueryWrapper<DailyDO>().eq("daily_id", dailyDO.getDailyId())));
        }
        List<MessageDO> messages = messageMapper.selectList(new QueryWrapper<MessageDO>().eq("user_name", userName));
        if (messages.size() != 0) {
            messages.stream()
                    .forEach(messageDO -> messageMapper.update(messageDO.setUserAvator(blogUserDO.getUserAvator())
                                    .setUserNetname(blogUserDO.getUserNetname()),
                            new QueryWrapper<MessageDO>().eq("message_id", messageDO.getMessageId())));
        }

        List<FriendDO> friends = friendMapper.selectList(new QueryWrapper<FriendDO>().eq("friend_username", userName));
        if (friends.size() != 0) {
            friends.stream()
                    .forEach(friendDO -> friendMapper.update(friendDO.setFriendAvator(blogUserDO.getUserAvator())
                                    .setFriendNetname(blogUserDO.getUserNetname()),
                            new QueryWrapper<FriendDO>().eq("friend_id", friendDO.getFriendId())));
        }
    }

}
