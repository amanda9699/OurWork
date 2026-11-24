package com.spring.teamwork.model.Enum;

import com.spring.teamwork.Exception.common.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 参数
    PARAM_INVALID(400, "参数校验失败"),

    // 401 认证
    NOT_LOGIN(401, "未登录或 token 已过期"),
    PASSWORD_WRONG(401, "用户名或密码错误"),

    // 403 权限
    NO_PERMISSION(403, "无操作权限"),

    // 404 资源
    USER_NOT_FOUND(404, "用户不存在"),

    // 409 冲突
    USERNAME_EXISTS(409, "用户名已被注册"),
    BALANCE_NOT_ENOUGH(409, "余额不足"),

    // 500 服务器
    SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    /** 抛 BizException */
    public void throwEx() {
        throw new BizException(this.message, this.code);
    }

    /** 用自定义消息覆盖默认文案 */
    public void throwEx(String overrideMsg) {
        throw new BizException(overrideMsg, this.code);
    }
}
