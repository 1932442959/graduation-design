package com.scu.lcw.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.lcw.blog.mapper.ArticleMapper;
import com.scu.lcw.blog.mapper.CommentMapper;
import com.scu.lcw.blog.mapper.DailyMapper;
import com.scu.lcw.blog.pojo.vo.CardVO;
import com.scu.lcw.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController extends BaseController {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private DailyMapper dailyMapper;

    @Resource
    private CommentMapper commentMapper;

    @RequestMapping("/card")
    public Result cardMessage() {
        return Result.data(new CardVO()
                .setArticleNum(articleMapper.selectCount(new QueryWrapper<>()))
                .setDailyNum(dailyMapper.selectCount(new QueryWrapper<>()))
                .setCommentNum(commentMapper.selectCount(new QueryWrapper<>())));
    }

}
