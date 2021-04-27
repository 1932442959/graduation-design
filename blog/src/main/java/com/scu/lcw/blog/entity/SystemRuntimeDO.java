package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@TableName("system_runtime")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SystemRuntimeDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long runTime;

}
