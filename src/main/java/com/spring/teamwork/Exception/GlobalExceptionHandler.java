package com.spring.teamwork.Exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.spring.teamwork.Exception.common.BizException;
import com.spring.teamwork.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public Result<?> exceptionHandler(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)   // 兜底的未知异常
    public Result<?> handleUnknown(Exception e) {
        e.printStackTrace(); // 打印堆栈便于排查问题
        return Result.fail(500, "服务器内部错误");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)  // @Valid 校验失败
    public Result<?> handleValid(MethodArgumentNotValidException e) {
        return Result.fail(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return Result.fail(400, "参数类型不合法: " + e.getName());
    }
    @ExceptionHandler(NotLoginException.class)  //sa-token未登录
    public Result<?> handleNotLogin(NotLoginException e){
        log.warn("[auth] 未登录拦截: type={}, loginType={}", e.getType(),e.getLoginType());
        return Result.fail(401, "未登录或 token 已过期");
    }
    /** Sa-Token 角色/权限不足 */
    @ExceptionHandler({NotRoleException.class, NotPermissionException.class})
    public Result<?> handleNoAuth(Exception e) {
        log.warn("[auth] 权限不足: {}", e.getMessage());
        return Result.fail(403, "无操作权限");
    }
    /** 缺少请求参数 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingParam(MissingServletRequestParameterException e) {
        return Result.fail(400, "缺少参数: " + e.getParameterName());
    }
}
