package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.ProgressRequest;
import com.scu.lcw.common.response.Result;

public interface ProgressService {

    Result getProgressList(ProgressRequest progressRequest);

}
