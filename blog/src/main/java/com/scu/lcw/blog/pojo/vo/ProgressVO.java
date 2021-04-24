package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.entity.ProgressDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProgressVO {

    private Integer total;

    private List<ProgressDO> progressList;

}
