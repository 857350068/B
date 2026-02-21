package com.hr.service.impl;

import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.UserMapper;
import com.hr.model.dto.UserDTO;
import com.hr.model.entity.User;
import com.hr.security.JwtUtil;
import com.hr.service.UserService;
import com.hr.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现
 * 依据：开题报告 3.2.1 节 - 用户登录、个人中心
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO login(String username, String password) {
        User user = userMapper.selectByUsername(username, 0);
        if (user == null) {
            throw new BaseException(ErrorCode.PARAM_ERROR, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(ErrorCode.PARAM_ERROR, "用户名或密码错误");
        }
        List<Long> deptScope = SecurityUtil.getDeptScope(user.getDeptScope());
        if (user.getDeptId() != null && deptScope.isEmpty()) {
            deptScope.add(user.getDeptId());
        }
        String token = JwtUtil.generate(username, user.getRole(), deptScope);
        UserDTO dto = new UserDTO();
        dto.setToken(token);
        dto.setUsername(username);
        dto.setRole(user.getRole());
        dto.setDeptScope(deptScope);
        dto.setDeptScopeList(deptScope);
        dto.setId(user.getId());
        return dto;
    }

    @Override
    public UserDTO getProfile(String username) {
        User user = userMapper.selectByUsername(username, 0);
        if (user == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setDeptId(user.getDeptId());
        dto.setDeptScopeList(SecurityUtil.getDeptScope(user.getDeptScope()));
        return dto;
    }
}
