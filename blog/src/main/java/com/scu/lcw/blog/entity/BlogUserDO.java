package com.scu.lcw.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scu.lcw.blog.pojo.request.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("blog_user")
public class BlogUserDO implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    private String userName;

    private String userPassword;

    private String userNetname;

    private String userLikeArticle;

    private String userDislikeArticle;

    private String userLikeComment;

    private String userDislikeComment;

    private LocalDateTime createTime;

    private String userEmail;

    private String enabled;

    private String userAvator;

    private String userLikeDaily;

    private String userDislikeDaily;

    public static BlogUserDO buildBlogUserDO(RegisterRequest registerRequest) {
        return new BlogUserDO()
                .setUserName(registerRequest.getRegisterName())
                .setUserPassword(registerRequest.getRegisterPassword())
                .setUserEmail(registerRequest.getEmail())
                .setCreateTime(LocalDateTime.now())
                .setEnabled("Y")
                .setUserNetname(registerRequest.getRegisterName())
                .setUserAvator("https://my-blog-lucw.oss-cn-beijing.aliyuncs.com/image/avator/默认头像.jpg?versionId=CAEQDhiBgMC06MHoxxciIDUyNDBkYTA3ZTY1NjQwMzc5ZmZjYmQzYzUyMzJkYjBm");
    }
}
