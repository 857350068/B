package com.hr.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.common.RoleEnum;
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
     * 获取当前用户ID
     * 
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            // 从JWT Token中获取用户信息
            // 在实际实现中，这里应该从JWT的claims中提取userId
            // 由于Spring Security JWT配置较复杂，这里简化处理
            // 通常在JWT中会包含userId、username等信息
            Object principal = auth.getPrincipal();
            if (principal instanceof String) {
                // 如果是简单字符串，可能是用户名
                // 在实际项目中，应该从JWT中解析出userId
                return null; // 简化实现
            }
            // 实际应用中需要从JWT Claims中获取userId
            // 这里返回null表示简化实现，需要在实际部署时完善
            return null;
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

    /**
     * 判断当前用户是否为管理员
     * 
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_HR_ADMIN"));
        }
        return false;
    }
}
