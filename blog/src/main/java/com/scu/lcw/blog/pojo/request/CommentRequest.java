package com.scu.lcw.blog.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentRequest {

    private Long parentId;

    private Long refrenceId;

    private String parentUsername;

    private String parentNetname;

    private String commentContent;

}
