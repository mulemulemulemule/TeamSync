package com.mule.demo.common;

import lombok.Data;

/**
 * 统一响应结果类 (Unified Response Object)
 */
@Data
public class Result<T> {

    /** 业务状态码 (200=成功, 500=失败) */
    private Integer code;

    /** 提示信息 (给用户看的) */
    private String msg;

    /** 实际的数据负载 (比如用户列表、Token等) */
    private T data;

    /**
     * 快速构建成功响应
     * @param data 要返回的数据
     * @param <T> 数据类型
     * @return 状态码为200的 Result 对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    /**
     * 快速构建失败响应
     * @param msg 错误提示信息
     * @param <T> 数据类型
     * @return 状态码为500的 Result 对象
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = msg;
        return result;
    }
}