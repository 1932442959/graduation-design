package com.scu.lcw.backsystem.Service;

import com.scu.lcw.backsystem.pojo.request.DailyRequest;
import com.scu.lcw.common.response.Result;

public interface DailyService {

    Result delete(DailyRequest dailyRequest);

    Result addDaily(DailyRequest dailyRequest);

    Result editDaily(DailyRequest dailyRequest);
}
