package com.scu.lcw.blog.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:51
 */
@Component
public class LocalDateUtils {

    public String parseCreateTime(LocalDateTime createTime) {
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String parseUpdateTime(LocalDateTime updateTime) {
        return updateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
