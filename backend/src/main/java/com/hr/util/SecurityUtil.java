package com.hr.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全工具类 - 获取当前登录用户信息
 * 依据：开题报告 3.2.1 节 - 部门范围过滤
 */
public class SecurityUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            return auth.getPrincipal().toString();
        }
        return null;
    }

    /**
     * 从 dept_scope JSON 解析部门ID列表
     * 用于 DEPT_HEAD 权限过滤：WHERE dept_id IN (dept_scope)
     */
    public static List<Long> getDeptScope(String deptScopeJson) {
        if (deptScopeJson == null || deptScopeJson.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return MAPPER.readValue(deptScopeJson, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
