package com.scu.lcw.blog.pojo.request;

import lombok.Data;

@Data
public class FriendRequest {

    private String inputUrl;

    private String inputIntro;

    private String blogUserLoginFlag;

    private Long friendId;

}
