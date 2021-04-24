package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.entity.LabelDO;
import com.scu.lcw.common.response.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LabelVO extends BaseVO {

    private Long labelId;

    private String labelName;

    private String labelColor;

    private Long parentId;

    private List<LabelVO> childList;

    private Integer articleNum;

    public static LabelVO buildLabelVO(LabelDO labelDO) {
        return new LabelVO()
                .setLabelId(labelDO.getLabelId())
                .setLabelName(labelDO.getLabelName())
                .setLabelColor(labelDO.getLabelColor())
                .setParentId(labelDO.getParentId());
    }

}
