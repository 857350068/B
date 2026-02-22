package com.hr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT 工具类
 * 依据：开题报告 3.2.1 节 - 用户登录返回 JWT token
 */
public class JwtUtil {

    private static final String SECRET = "hr-datacenter-jwt-secret-key-2026-springboot";
    private static final long EXPIRE_MS = 24 * 60 * 60 * 1000L; // 24小时

    public static String generate(Long userId, String username, String role, List<Long> deptScope) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRE_MS);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .claim("deptScope", deptScope != null ? deptScope.toString() : "[]")
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token) {
        return parse(token).getSubject();
    }
    
    public static Long getUserId(String token) {
        Object userIdObj = parse(token).get("userId");
        if (userIdObj != null) {
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else {
                return Long.parseLong(userIdObj.toString());
            }
        }
        return null;
    }
}
