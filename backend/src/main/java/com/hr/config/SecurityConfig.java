package com.hr.config;

import com.hr.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * RBAC 权限控制核心配置
 * 依据：开题报告 1.2 节"数据安全"、3.2.1/3.2.2 节角色权限
 *
 * 权限说明：
 * - /api/auth/** 放行（登录注册）
 * - /admin/** 仅 HR_ADMIN
 * - /api/data/** HR_ADMIN、DEPT_HEAD、MANAGEMENT
 * - /api/warning/** HR_ADMIN、DEPT_HEAD
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/admin/**").hasRole("HR_ADMIN")
                .antMatchers("/api/warning/**").hasAnyRole("HR_ADMIN", "DEPT_HEAD")
                .antMatchers("/api/analysis/**").hasAnyRole("HR_ADMIN", "DEPT_HEAD", "MANAGEMENT")
                .antMatchers("/api/**").hasAnyRole("HR_ADMIN", "DEPT_HEAD", "MANAGEMENT", "EMPLOYEE")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
