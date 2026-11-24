package com.spring.teamwork.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/*注册入参*/
@Data
public class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(max = 30, message = "用户名最长30字符")
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 32, message = "密码长度6~32位")
        private String password;
}

