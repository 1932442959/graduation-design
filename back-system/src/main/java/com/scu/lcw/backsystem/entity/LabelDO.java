package com.scu.lcw.backsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.common.response.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("label")
public class LabelDO extends BaseVO {

    @TableId(value = "label_id", type = IdType.AUTO)
    private Long labelId;

    private String labelName;

    private String labelColor;

    private Long parentId;

}
