package com.scu.lcw.blog.pojo.request;

import lombok.Data;

@Data
public class LabelRequest {

    private String labelName;

    private String labelColor;

    private Long parentId;

    private Long labelId;

}
