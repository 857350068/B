package com.hr.service;

import com.hr.model.dto.UserDTO;

/**
 * 用户服务接口
 * 依据：开题报告 3.2.1 节
 */
public interface UserService {

    /**
     * 登录：校验密码，返回 token、role、deptScope
     */
    UserDTO login(String username, String password);

    /**
     * 获取当前用户信息
     */
    UserDTO getProfile(String username);
}
