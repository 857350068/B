package com.hr.service;

import com.hr.model.dto.PageResult;
import com.hr.model.dto.UserAdminDTO;
import com.hr.model.dto.UserDTO;
import com.hr.model.entity.User;

/**
 * 用户服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
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
    
    /**
     * 获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserInfo(Long id);
    
    /**
     * 更新用户密码
     * 
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    boolean updatePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 获取用户分页列表
     * 
     * @param keyword 关键词搜索
     * @param role 角色筛选
     * @param deptId 部门筛选
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户分页结果
     */
    PageResult<UserAdminDTO> getUserPage(String keyword, String role, Long deptId, Long pageNum, Long pageSize);
    
    /**
     * 根据ID获取用户信息（管理端）
     * 
     * @param id 用户ID
     * @return 用户管理DTO
     */
    UserAdminDTO getUserAdminById(Long id);
    
    /**
     * 添加新用户
     * 
     * @param userAdminDTO 用户信息
     * @return 用户信息
     */
    UserAdminDTO addUser(UserAdminDTO userAdminDTO);
    
    /**
     * 更新用户信息
     * 
     * @param userAdminDTO 用户信息
     * @return 是否更新成功
     */
    boolean updateUser(UserAdminDTO userAdminDTO);
    
    /**
     * 删除用户（逻辑删除）
     * 
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long id);
    
    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表
     * @return 删除成功的数量
     */
    int batchDeleteUsers(java.util.List<Long> ids);
}
