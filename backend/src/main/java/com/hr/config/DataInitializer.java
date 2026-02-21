package com.hr.config;

import com.hr.mapper.UserMapper;
import com.hr.model.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化 - 首次启动创建 admin 账号
 * 依据：doc/注意事项 - 验证 admin/123456 登录
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userMapper.selectByUsername("admin", 0) == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("HR_ADMIN");
            admin.setDeptId(103L);
            admin.setDeptScope("[101,102,103,104]");
            admin.setIsDeleted(0);
            userMapper.insert(admin);
        }
    }
}
