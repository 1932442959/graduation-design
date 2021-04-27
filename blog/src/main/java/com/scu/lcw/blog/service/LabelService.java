package com.scu.lcw.blog.service;

import com.scu.lcw.blog.pojo.request.LabelRequest;
import com.scu.lcw.common.response.Result;

public interface LabelService {

    Result getLabelList();

    Result getAllLabelList();

    Result addParent(LabelRequest labelRequest);

    Result addChild(LabelRequest labelRequest);

    Result update(LabelRequest labelRequest);

    Result delete(LabelRequest labelRequest);
}
