package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("message")
public class MessageDO {

    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId;

    private String userName;

    private String userNetname;

    private String userEmail;

    private String userAvator;

    private String messageContent;

    private LocalDateTime createTime;

    public static MessageDO buildMessageDO(BlogUserDO blogUserDO, String messageContent) {
        return new MessageDO()
                .setUserName(blogUserDO.getUserName())
                .setUserNetname(blogUserDO.getUserNetname())
                .setUserEmail(blogUserDO.getUserEmail())
                .setUserAvator(blogUserDO.getUserAvator())
                .setMessageContent(messageContent)
                .setCreateTime(LocalDateTime.now());
    }
}
