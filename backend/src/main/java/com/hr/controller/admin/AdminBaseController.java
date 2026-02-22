package com.hr.controller.admin;

import com.hr.common.Response;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理基础控制器
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RequestMapping("/api/admin")
public abstract class AdminBaseController {
    
    /**
     * 返回成功响应
     */
    protected <T> Response<T> success(T data) {
        return Response.success(data);
    }
    
    /**
     * 返回成功响应（无数据）
     */
    protected Response<Void> success() {
        return Response.success(null);
    }
    
    /**
     * 返回错误响应
     */
    protected <T> Response<T> error(int code, String message) {
        return Response.error(code, message);
    }
    
    /**
     * 返回操作失败响应
     */
    protected <T> Response<T> fail(String message) {
        return Response.error(500, message);
    }
}