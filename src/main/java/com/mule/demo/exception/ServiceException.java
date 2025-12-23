package com.mule.demo.exception;

import lombok.Getter;

/**
 * 自定义业务异常 (Custom Business Exception)
 * <p>
 * 这是我们专门用来“中断业务流程”的异常类。
 * 当业务逻辑无法继续（比如：余额不足、用户名重复）时，
 * 我们不应该 `return null`，而应该 `throw new ServiceException("提示信息")`。
 * 
 * 全局异常处理器会捕获它，并把提示信息返回给前端。
 * </p>
 */
@Getter
public class ServiceException extends RuntimeException {
    
    /** 错误码 (可选，默认500) */
    private Integer code;

    /**
     * 构造一个默认错误码(500)的异常
     * @param msg 错误提示信息
     */
    public ServiceException(String msg) {
        super(msg);
        this.code = 500;
    }

    /**
     * 构造一个指定错误码的异常
     * @param code 错误码 (如 401, 403)
     * @param msg 错误提示信息
     */
    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}