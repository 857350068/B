package com.hr.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户实体
 * 依据：开题报告 3.2.1 节、后端开发设计文档
 */
@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String role;
    private Long deptId;
    private String deptScope;
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;
}
