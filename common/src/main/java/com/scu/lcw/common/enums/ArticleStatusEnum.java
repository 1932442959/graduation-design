package com.scu.lcw.common.enums;

public enum ArticleStatusEnum {

    PUBLISH("发布"),

    DRAFT("草稿");

    private String statusName;

    ArticleStatusEnum(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
