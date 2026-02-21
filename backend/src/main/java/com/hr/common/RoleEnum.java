package com.hr.common;

import lombok.Getter;

/**
 * 角色枚举
 * 依据：开题报告 3.2.1 节 - 4类角色权限
 */
@Getter
public enum RoleEnum {

    HR_ADMIN("HR_ADMIN", "HR管理员"),
    DEPT_HEAD("DEPT_HEAD", "部门负责人"),
    MANAGEMENT("MANAGEMENT", "企业管理层"),
    EMPLOYEE("EMPLOYEE", "普通员工");

    private final String code;
    private final String desc;

    RoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
