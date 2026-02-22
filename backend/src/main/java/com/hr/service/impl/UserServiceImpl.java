package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.UserMapper;
import com.hr.model.dto.PageResult;
import com.hr.model.dto.UserAdminDTO;
import com.hr.model.dto.UserDTO;
import com.hr.model.entity.User;
import com.hr.security.JwtUtil;
import com.hr.service.UserService;
import com.hr.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        String token = JwtUtil.generate(user.getId(), username, user.getRole(), deptScope);
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
    
    @Override
    public User getUserInfo(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return user;
    }
    
    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "旧密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "新密码不能为空");
        }
        if (newPassword.equals(oldPassword)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "新密码不能与旧密码相同");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BaseException(ErrorCode.PARAM_ERROR, "旧密码不正确");
        }
        
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        
        int result = userMapper.updateById(user);
        return result > 0;
    }
    
    @Override
    public PageResult<UserAdminDTO> getUserPage(String keyword, String role, Long deptId, Long pageNum, Long pageSize) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getName, keyword));
        }
        
        if (role != null && !role.trim().isEmpty()) {
            queryWrapper.eq(User::getRole, role);
        }
        
        if (deptId != null && deptId > 0) {
            queryWrapper.eq(User::getDeptId, deptId);
        }
        
        queryWrapper.eq(User::getIsDeleted, 0); // 只查询未删除的用户
        
        // 查询数据库
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = userMapper.selectPage(page, queryWrapper);
        
        // 转换为UserAdminDTO
        List<UserAdminDTO> userAdminDTOList = new ArrayList<>();
        for (User user : result.getRecords()) {
            UserAdminDTO dto = new UserAdminDTO();
            BeanUtils.copyProperties(user, dto);
            userAdminDTOList.add(dto);
        }
        
        return PageResult.of(pageNum, pageSize, result.getTotal(), userAdminDTOList);
    }
    
    @Override
    public UserAdminDTO getUserAdminById(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        
        UserAdminDTO dto = new UserAdminDTO();
        BeanUtils.copyProperties(user, dto);
        
        return dto;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAdminDTO addUser(UserAdminDTO userAdminDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(userAdminDTO.getUsername(), 0);
        if (existingUser != null) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }
        
        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(userAdminDTO, user);
        // 密码加密
        String encodedPassword = passwordEncoder.encode("123456"); // 默认密码
        user.setPassword(encodedPassword);
        user.setCreateTime(new java.util.Date());
        user.setUpdateTime(new java.util.Date());
        user.setStatus(userAdminDTO.getStatus() != null ? userAdminDTO.getStatus() : 1); // 默认启用
        user.setIsDeleted(0); // 默认未删除
        
        // 保存到数据库
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "添加用户失败");
        }
        
        // 返回用户信息
        UserAdminDTO resultDto = new UserAdminDTO();
        BeanUtils.copyProperties(user, resultDto);
        
        return resultDto;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserAdminDTO userAdminDTO) {
        if (userAdminDTO.getId() == null || userAdminDTO.getId() <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        // 检查用户是否存在
        User existingUser = userMapper.selectById(userAdminDTO.getId());
        if (existingUser == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        
        // 检查用户名是否与其他用户冲突
        User conflictUser = userMapper.selectByUsernameExcludeId(userAdminDTO.getUsername(), userAdminDTO.getId());
        if (conflictUser != null) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }
        
        // 更新用户信息
        BeanUtils.copyProperties(userAdminDTO, existingUser, "password", "createTime");
        existingUser.setUpdateTime(new java.util.Date());
        
        int result = userMapper.updateById(existingUser);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        // 检查用户是否存在
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        
        // 逻辑删除 - 设置为已删除
        existingUser.setIsDeleted(1); // 设置为已删除
        existingUser.setUpdateTime(new java.util.Date());
        
        int result = userMapper.updateById(existingUser);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            try {
                if (deleteUser(id)) {
                    deletedCount++;
                }
            } catch (Exception e) {
                // 记录错误但继续处理其他用户
                System.err.println("删除用户失败，ID: " + id + ", 错误: " + e.getMessage());
            }
        }
        
        return deletedCount;
    }
}
