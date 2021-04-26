package com.scu.lcw.common.enums;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 14:48
 */
public enum CommentTypeEnum {

    DAILY("日记评论"),

    ARTICLE("文章评论");

    private String commentType;

    CommentTypeEnum(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }
}
