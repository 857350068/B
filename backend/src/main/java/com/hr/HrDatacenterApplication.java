package com.hr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 人力资源数据中心系统 - 主启动类
 * 依据：开题报告 2.1 节技术框架、4.1.3 节系统分层架构
 */
@SpringBootApplication
@MapperScan("com.hr.mapper")
public class HrDatacenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrDatacenterApplication.class, args);
    }
}
