package com.scu.lcw.blog.pojo.bo;

import com.scu.lcw.blog.entity.ArticleDO;
import com.scu.lcw.blog.entity.DailyDO;
import com.scu.lcw.blog.pojo.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleBO {

    private ArticleDO articleDO;

    private Boolean showComment;

    private List<CommentDTO> commentList;

}
