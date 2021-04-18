package com.scu.lcw.blog.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterRequest {

    private String registerName;

    private String registerPassword;

    private String repeatPassword;

    private String email;

    private String validateEmail;
}
