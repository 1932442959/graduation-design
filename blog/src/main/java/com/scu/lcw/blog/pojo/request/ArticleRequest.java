package com.scu.lcw.blog.pojo.request;

import com.scu.lcw.blog.pojo.vo.LabelVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import page.BasePage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleRequest extends BasePage {

    private String labelName;

}
