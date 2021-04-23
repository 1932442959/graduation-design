package com.scu.lcw.blog.pojo.vo;

import com.scu.lcw.blog.entity.MessageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageVO {

    private List<MessageDO> messageList;

    private Integer total;

}
