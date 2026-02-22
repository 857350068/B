package com.hr.common;

/**
 * 错误码定义
 * 依据：后端开发设计文档 七、异常处理设计
 */
public class ErrorCode {

    public static final int SUCCESS = 200;
    public static final int PARAM_ERROR = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;
    
    // 扩展错误码
    public static final int PARAMS_ERROR = 400;
    public static final int OPERATION_ERROR = 500;
    public static final int NO_AUTH_ERROR = 403;
}
