package com.scu.lcw.backsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("report")
public class ReportDO {

    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    private String reportTitle;

    private String reportContent;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String dateTime;
}
