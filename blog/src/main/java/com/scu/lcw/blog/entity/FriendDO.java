package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("friend")
public class FriendDO {

    @TableId(value = "friend_id", type = IdType.AUTO)
    private Long friendId;

    private String friendUsername;

    private String friendNetname;

    private String friendAvator;

    private String friendIntro;

    private String friendUrl;

}
