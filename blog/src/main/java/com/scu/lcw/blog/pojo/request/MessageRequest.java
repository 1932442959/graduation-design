package com.scu.lcw.blog.pojo.request;

import lombok.Data;
import page.BasePage;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:20
 */
@Data
public class MessageRequest extends BasePage {

    private String messageContent;
}
