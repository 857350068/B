package com.hr.controller.admin;

import com.hr.common.Response;
import com.hr.model.dto.PageResult;
import com.hr.model.dto.UserAdminDTO;
import com.hr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户管理相关的API接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController extends AdminBaseController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserAdminController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 查询所有用户（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词搜索
     * @param role 角色筛选
     * @param deptId 部门筛选
     * @return 用户分页列表
     */
    @GetMapping
    public Response<PageResult<UserAdminDTO>> getUsers(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long deptId) {
        try {
            PageResult<UserAdminDTO> result = userService.getUserPage(keyword, role, deptId, pageNum, pageSize);
            return success(result);
        } catch (Exception e) {
            log.error("查询用户列表失败: ", e);
            return error(500, "查询用户列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Response<UserAdminDTO> getUserById(@PathVariable Long id) {
        try {
            UserAdminDTO user = userService.getUserAdminById(id);
            return success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败: id={}", id, e);
            return error(500, "获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加新用户
     * 
     * @param userAdminDTO 用户信息
     * @return 添加结果
     */
    @PostMapping
    public Response<UserAdminDTO> addUser(@RequestBody UserAdminDTO userAdminDTO) {
        try {
            UserAdminDTO user = userService.addUser(userAdminDTO);
            return success(user);
        } catch (Exception e) {
            log.error("添加用户失败: ", e);
            return error(500, "添加用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param userAdminDTO 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Response<Void> updateUser(@PathVariable Long id, @RequestBody UserAdminDTO userAdminDTO) {
        try {
            userAdminDTO.setId(id);
            boolean result = userService.updateUser(userAdminDTO);
            return result ? success() : error(500, "更新用户失败");
        } catch (Exception e) {
            log.error("更新用户失败: id={}", id, e);
            return error(500, "更新用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户（逻辑删除）
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Response<Void> deleteUser(@PathVariable Long id) {
        try {
            boolean result = userService.deleteUser(id);
            return result ? success() : error(500, "删除用户失败");
        } catch (Exception e) {
            log.error("删除用户失败: id={}", id, e);
            return error(500, "删除用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表
     * @return 删除结果
     */
    @DeleteMapping
    public Response<Void> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            int result = userService.batchDeleteUsers(ids);
            return success();
        } catch (Exception e) {
            log.error("批量删除用户失败: ", e);
            return error(500, "批量删除用户失败: " + e.getMessage());
        }
    }
}