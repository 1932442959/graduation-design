package com.scu.lcw.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("article")
public class Article {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String toc;
    private String content;
    private String firstPicture;
}
