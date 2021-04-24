package com.scu.lcw.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CardVO {

    private Integer commentNum;

    private Integer dailyNum;

    private Integer articleNum;

}
