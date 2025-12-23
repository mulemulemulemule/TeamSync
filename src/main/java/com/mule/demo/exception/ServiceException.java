package com.mule.demo.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class ServiceException extends RuntimeException {
    

    private Integer code;


    public ServiceException(String msg) {
        super(msg);
        this.code = 500;
    }


    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}