package com.scu.lcw.blog.pojo.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DailyLikeRequest {

    private Long dailyId;

    private Long dailyLike;

    private Long dailyDislike;

    private String dailyContent;

    private String dailyAvator;

    private String dailyUsername;

    private String dailyNetname;

    private Integer commentLength;

    private String date;

    private String dateTime;

    private String blogUserLoginFlag;
}
