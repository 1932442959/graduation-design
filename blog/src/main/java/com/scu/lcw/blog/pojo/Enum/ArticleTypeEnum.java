package com.scu.lcw.blog.pojo.Enum;

public enum ArticleTypeEnum {

    //转载
    REPRINT("reprint", "转载"),

    //原创
    ORIGINAL("original", "原创");

    private String type;

    private String name;

    ArticleTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
