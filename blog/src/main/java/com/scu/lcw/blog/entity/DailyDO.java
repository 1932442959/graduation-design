package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.blog.pojo.bo.DailyBO;
import com.scu.lcw.blog.pojo.request.DailyLikeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 13:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("daily")
public class DailyDO {

    @TableId(value = "daily_id", type = IdType.AUTO)
    private Long dailyId;

    private Long dailyLike;

    private Long dailyDislike;

    private String dailyContent;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String dailyAvator;

    private String dailyUsername;

    private String dailyNetname;

    @TableField(exist = false)
    private String blogUserLoginFlag;

    @TableField(exist = false)
    private Integer commentLength;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String dateTime;

    public static DailyBO buildDailyVO(DailyDO dailyDO) {
        return new DailyBO().setDailyDO(dailyDO);
    }

    public static DailyDO buildDailyDO(DailyLikeRequest dailyLikeRequest) {
        return new DailyDO()
                .setDailyId(dailyLikeRequest.getDailyId())
                .setDailyLike(dailyLikeRequest.getDailyLike())
                .setDailyDislike(dailyLikeRequest.getDailyDislike())
                .setDailyContent(dailyLikeRequest.getDailyContent())
                .setDailyAvator(dailyLikeRequest.getDailyAvator())
                .setDailyUsername(dailyLikeRequest.getDailyUsername())
                .setDailyNetname(dailyLikeRequest.getDailyNetname());
    }
}
