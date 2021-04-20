package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.ReportRequest;
import com.scu.lcw.common.response.Result;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:26
 */
public interface ReportService {

    Result findAllReport(ReportRequest reportRequest);

}
