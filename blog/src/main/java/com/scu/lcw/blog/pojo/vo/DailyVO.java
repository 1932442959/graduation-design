package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.pojo.bo.DailyBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 17:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DailyVO {

    private Integer total;

    private List<DailyBO> dailyList;
}
