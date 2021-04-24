package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("progress")
public class ProgressDO {

    @TableId(value = "progress_id", type = IdType.AUTO)
    private Long progressId;

    private String progressContent;

    private LocalDateTime createTime;

    private String progressNetname;

    private String progressAvator;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String dateTime;

}
