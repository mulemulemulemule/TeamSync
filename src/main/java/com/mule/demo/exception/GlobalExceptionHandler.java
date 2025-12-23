package com.mule.demo.exception;

import com.mule.demo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器 (Global Exception Handler)
 * <p>
 * 这是一个“兜底”的类。
 * 它的作用是监听整个项目里抛出的所有异常。
 * 一旦有代码报错（比如空指针、业务报错、参数校验失败），
 * 这里的方法就会捕获它，并把它转换成一个友好的 {@link Result} JSON 返回给前端。
 * 
 * 这样前端永远不会收到恶心的 500 报错页面。
 * </p>
 */
@Slf4j // 自动注入 log 对象，用于打印日志
@RestControllerAdvice // 标记这是一个全局处理类，对所有 Controller 生效
public class GlobalExceptionHandler {

    /**
     * 捕获我们自己抛出的业务异常
     * (比如：throw new ServiceException("用户名已存在"))
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        log.error("Business Error: {}", e.getMessage());
        // 构建 Result，优先使用异常里的错误码，没有则用 500
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
        // 提取第一条错误提示信息
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
        // 打印完整的堆栈日志，方便我们在后台排查 Bug
        log.error("System Error: ", e);
        // 但给用户只返回一个模糊的提示，防止泄露系统细节
        return Result.error("System busy, please try again later");
    }
}