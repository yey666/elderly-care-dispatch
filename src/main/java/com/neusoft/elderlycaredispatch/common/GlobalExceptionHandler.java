package com.neusoft.elderlycaredispatch.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 专门拦截找不到资源的异常（404）
     * 对于 /doc.html 相关的请求，直接放过，不返回系统繁忙，避免文档页面加载失败
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFound(NoResourceFoundException e) {
        String path = e.getResourcePath();
        // 如果是文档相关的请求或浏览器插件乱发的请求，不打印错误日志，也不返回 500
        if (path.contains("doc.html") || path.contains("webjars") || path.contains("v3/api-docs") || path.contains("swagger")) {
            log.debug("放行文档静态资源请求：{}", path);
            return Result.error(404, "静态资源未找到");
        }
        log.warn("找不到请求路径：{}", path);
        return Result.error(404, "接口不存在");
    }

    /**
     * 拦截其他未知异常（如空指针等）
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统繁忙，请稍后重试");
    }
}