package com.spring.teamwork.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthRequest {
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 4, max = 20, message = "用户名 4~20 位")
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码 6~20 位")
        private String password;

        @NotBlank(message = "昵称不能为空")
        @Size(max = 30, message = "昵称最长 30 字符")
        private String nickname;
    }

    /** 登录（接口文档 2.2） */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

}
