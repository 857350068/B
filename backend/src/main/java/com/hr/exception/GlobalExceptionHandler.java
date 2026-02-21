package com.hr.exception;

import com.hr.common.ErrorCode;
import com.hr.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 依据：后端开发设计文档 七、异常处理设计
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Response<?> handleBaseException(BaseException e) {
        return Response.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response<?> handleAccessDenied(AccessDeniedException e) {
        return Response.error(ErrorCode.FORBIDDEN, "权限不足");
    }

    /** JSON 解析失败（如 PowerShell 引号转义导致格式错误） */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<?> handleJsonParse(HttpMessageNotReadableException e) {
        log.warn("请求体JSON格式错误: {}", e.getMessage());
        return Response.error(ErrorCode.PARAM_ERROR, "请求体JSON格式错误，请检查Content-Type和JSON格式");
    }

    /** 请求方法不支持（如用GET访问POST接口） */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return Response.error(405, "请求方法不支持");
    }

    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Response.error(ErrorCode.SERVER_ERROR, "系统内部错误");
    }
}
