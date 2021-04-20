package com.scu.lcw.blog.pojo.bo;

import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DailyBO {

    private DailyDO dailyDO;

    private List<CommentDTO> commentList;

}
