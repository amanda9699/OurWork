package com.spring.teamwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) { return new Result<>(0, "success", data); }
    public static Result<Void> ok()        { return new Result<>(0, "success", null); }
    public static Result<Void> fail(int code, String msg) { return new Result<>(code, msg, null); }
}
