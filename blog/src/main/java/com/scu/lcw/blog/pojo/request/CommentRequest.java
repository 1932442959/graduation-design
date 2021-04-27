package com.scu.lcw.blog.pojo.request;

import com.scu.lcw.common.page.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentRequest extends BasePage {

    private Long commentId;

    private Long parentId;

    private Long refrenceId;

    private String parentUsername;

    private String parentNetname;

    private String commentContent;

    private String blogUserLoginFlag;

}
