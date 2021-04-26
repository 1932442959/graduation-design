package com.scu.lcw.backsystem.pojo.request;

import com.scu.lcw.common.page.BasePage;
import lombok.Data;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:54
 */
@Data
public class DailyRequest extends BasePage {

    private Long dailyId;

    private String dailyContent;

}
