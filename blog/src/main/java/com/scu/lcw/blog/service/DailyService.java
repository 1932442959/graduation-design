package com.scu.lcw.blog.service;

import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.request.DailyRequest;
import com.scu.lcw.common.response.Result;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:51
 */
public interface DailyService {

    Result findAllDaily(DailyRequest dailyRequest);

    Result likeDaily(DailyDO dailyD, String blogUserLoginFlag);

    Result dislikeDaily(DailyDO dailyDO, String blogUserLoginFlag);

    Result getLikeDailyList(String blogUserLoginFlag);

    Result getDislikeDailyList(String blogUserLoginFlag);
}
