package com.scu.lcw.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.lcw.blog.mapper.AdminerMapper;
import com.scu.lcw.blog.mapper.ArticleMapper;
import com.scu.lcw.blog.pojo.AdminerDO;
import com.scu.lcw.blog.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/firstpage")
public class FirstPage {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AdminerMapper adminerMapper;

    @Resource
    private ArticleMapper articleMapper;

    @RequestMapping("/first")
    public String first() {
        List<AdminerDO> adminerDOS = adminerMapper.selectList(new QueryWrapper<>());
        return adminerDOS.get(0).getPassword();
    }

    @RequestMapping("/second")
    public List<Article> second() {
        List<Article> records = articleMapper.selectPage(
                new Page<Article>().setCurrent(1).setSize(10L), new QueryWrapper<>()).getRecords();
        return records;
    }

    @RequestMapping("/third")
    public List<Article> third() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("lcw", "lucongwen");
        System.out.println(valueOperations.get("lcw"));
        return articleMapper.findAll();
    }
}
