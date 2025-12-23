package com.mule.demo.exception;

import com.mule.demo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获我们自己抛出的业务异常
     * (比如：throw new ServiceException("用户名已存在"))
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        log.error("Business Error: {}", e.getMessage());
        Result<?> result = new Result<>();
        result.setCode(e.getCode() != null ? e.getCode() : 500);
        result.setMsg(e.getMessage());
        return result;
    }

    /**
     * 捕获参数校验异常
     * (比如：@NotBlank 校验失败时抛出的异常)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {

        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Failed: {}", msg);
        return Result.error(msg);
    }

    /**
     * 捕获所有未知的系统异常 (兜底)
     * (比如：NullPointerException, SQLSyntaxError)
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {

        log.error("System Error: ", e);

        return Result.error("System busy, please try again later");
    }
}