package com.hr.model.dto;

import java.util.List;

/**
 * 用户 DTO
 * 依据：开题报告 3.2.1 节
 * 注意：薪资等敏感字段需脱敏
 */
public class UserDTO {

    private Long id;
    private String username;
    private String role;
    private Long deptId;
    private String deptName;
    private List<Long> deptScope;

    /** 登录响应：JWT token */
    private String token;

    /** 部门范围（用于前端权限控制） */
    private List<Long> deptScopeList;

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

    public List<Long> getDeptScope() {
        return deptScope;
    }

    public void setDeptScope(List<Long> deptScope) {
        this.deptScope = deptScope;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Long> getDeptScopeList() {
        return deptScopeList;
    }

    public void setDeptScopeList(List<Long> deptScopeList) {
        this.deptScopeList = deptScopeList;
    }
}
