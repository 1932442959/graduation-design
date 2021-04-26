package com.scu.lcw.backsystem.pojo.request;

import com.scu.lcw.common.page.BasePage;
import lombok.Data;

@Data
public class ProgressRequest extends BasePage {

    private Long progressId;

    private String progressContent;
}
