package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.entity.CommentDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVO {

    private List<CommentDO> commentList;

    private Integer total;
}
