package com.spring.teamwork.model.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserVO {
    /** 注册响应（接口文档 2.1） */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterVO {
        private Long userId;
        private String token;
        private Integer giftAmount;
    }

    /** 登录响应（接口文档 2.2） */
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LoginVO {
        private Long userId;
        private String nickname;
        private String token;
        private Integer balance;
        private Integer frozen;
    }

    /** 当前用户信息（接口文档 2.4） */
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserProfileVO {
        private Long userId;
        private String username;
        private String nickname;
        private String avatar;
        private Integer balance;
        private Integer frozen;
        private LocalDateTime createdAt;
    }
}
