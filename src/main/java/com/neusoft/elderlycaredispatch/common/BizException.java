package com.neusoft.elderlycaredispatch.common;

/**
 * 自定义业务异常类
 */
public class BizException extends RuntimeException {

    private Integer code = 500;

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}