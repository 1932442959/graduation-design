package com.scu.lcw.backsystem.Service;

import com.scu.lcw.backsystem.pojo.request.DailyRequest;
import com.scu.lcw.backsystem.pojo.request.ProgressRequest;
import com.scu.lcw.common.response.Result;

public interface ProgressService {

    Result delete(ProgressRequest progressRequest);

    Result addProgress(ProgressRequest progressRequest);

    Result editProgress(ProgressRequest progressRequest);

}
