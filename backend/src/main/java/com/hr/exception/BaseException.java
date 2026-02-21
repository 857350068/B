package com.hr.exception;

import com.hr.common.ErrorCode;
import lombok.Getter;

/**
 * 基础业务异常
 * 依据：开题报告 3.3 节、后端开发设计文档 七、异常处理设计
 */
@Getter
public class BaseException extends RuntimeException {

    private final int code;
    private final String msg;

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(String msg) {
        this(ErrorCode.PARAM_ERROR, msg);
    }
}
