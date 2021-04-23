package com.scu.lcw.blog.service;

import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.blog.pojo.vo.DailyVO;
import com.scu.lcw.common.response.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:51
 */
public interface DailyService {

    Result findAllDaily(DailyRequest dailyRequest);

    Result likeDaily(DailyDO dailyD, HttpServletRequest request);

    Result dislikeDaily(DailyDO dailyDO, HttpServletRequest request);

    Result getLikeDailyList(HttpServletRequest request);

    Result getDislikeDailyList(HttpServletRequest request);
}
