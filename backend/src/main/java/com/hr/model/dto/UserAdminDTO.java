package com.hr.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户管理DTO类
 * 用于后台管理用户信息的传输
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public class UserAdminDTO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空")
    @Pattern(regexp = "^\\d{4,10}$", message = "工号必须是4-10位数字")
    private String username;
    
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    /**
     * 角色
     */
    @NotBlank(message = "角色不能为空")
    private String role;
    
    /**
     * 部门ID
     */
    @NotNull(message = "部门不能为空")
    private Long deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 状态 (0-禁用, 1-启用)
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}