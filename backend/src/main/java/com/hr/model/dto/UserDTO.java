package com.hr.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户 DTO
 * 依据：开题报告 3.2.1 节
 * 注意：薪资等敏感字段需脱敏
 */
@Data
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
}
