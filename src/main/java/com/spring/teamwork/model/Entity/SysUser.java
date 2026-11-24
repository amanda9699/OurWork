package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private String username;

    /** BCrypt 密文。序列化时禁止输出 */
    @JsonIgnore
    private String password;

    private String nickname;

    private String avatar;

    /** 可用余额 */
    private Integer balance;

    /** 冻结金额 */
    private Integer frozen;

    @TableLogic
    private Integer isDeleted;

}
