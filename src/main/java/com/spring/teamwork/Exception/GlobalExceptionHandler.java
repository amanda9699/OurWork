package com.spring.teamwork.Exception;

import com.spring.teamwork.Exception.common.BizException;
import com.spring.teamwork.model.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

}
