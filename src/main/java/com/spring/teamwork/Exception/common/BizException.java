package com.spring.teamwork.Exception.common;

public class BizException extends RuntimeException{
    private final int code;

    public BizException(String message,Integer code){
        super(message);
        this.code = code;
    }
    public int getCode(){
        return code;
    }

}
