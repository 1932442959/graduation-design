package com.scu.lcw.backsystem.pojo.vo;

import com.scu.lcw.backsystem.entity.LabelDO;
import com.scu.lcw.common.enums.ArticleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleConfigVO {

    private List<String> labelList;

}
