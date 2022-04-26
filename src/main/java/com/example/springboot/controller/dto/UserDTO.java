package com.example.springboot.controller.dto;

import lombok.Data;

/*
* 接受前端登录请求的参数
* */

@Data
public class UserDTO {
    private String uLoginName;
    private String uPassword;
    private String uNickname;
    private String uAvatarPath;
    private String token;
}
