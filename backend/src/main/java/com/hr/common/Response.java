package com.hr.common;

import lombok.Data;

/**
 * 统一响应格式
 * 依据：后端开发设计文档 - 所有接口返回 { code, msg, data }
 */
@Data
public class Response<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Response<T> success(T data) {
        Response<T> r = new Response<>();
        r.setCode(ErrorCode.SUCCESS);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> error(int code, String msg) {
        Response<T> r = new Response<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}
