package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.entity.ArticleDO;
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
public class ArticleVO extends BaseVO {

    private Integer total;

    private List<ArticleDO> articleList;

}
