package com.hr.controller;

import com.hr.common.Response;
import com.hr.model.dto.UserDTO;
import com.hr.service.UserService;
import com.hr.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器 - 登录/注册/个人中心
 * 依据：开题报告 3.2.1 节
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * 返回：token、role、deptScope
     */
    @PostMapping("/login")
    public Response<UserDTO> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return Response.error(400, "用户名和密码不能为空");
        }
        UserDTO dto = userService.login(username, password);
        return Response.success(dto);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public Response<UserDTO> profile() {
        String username = SecurityUtil.getCurrentUsername();
        if (username == null) {
            return Response.error(401, "未登录");
        }
        UserDTO dto = userService.getProfile(username);
        return Response.success(dto);
    }
}
