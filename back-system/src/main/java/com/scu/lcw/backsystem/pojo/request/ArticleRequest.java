package com.scu.lcw.backsystem.pojo.request;

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
public class ArticleRequest {

    private Long articleId;

    private String title;

    private String description;

    private String label;

    private ArticleTypeEnum typeEnum;

    private String password;

    private String coverImgUrl;

    private String firstImgUrl;

    private String content;

}
