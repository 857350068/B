-- 人力资源数据中心系统 - MySQL 初始化脚本
-- 依据：项目功能完整设计、后端开发设计文档

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hr_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hr_db;

-- 创建用户（需手动执行，或使用 root 执行）
-- CREATE USER 'hr_user'@'%' IDENTIFIED BY 'hr_password';
-- GRANT ALL PRIVILEGES ON hr_db.* TO 'hr_user'@'%';
-- FLUSH PRIVILEGES;

-- 1. 用户表 (开题报告 3.2.1 节)
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '工号',
    password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
    role VARCHAR(20) NOT NULL COMMENT 'HR_ADMIN/DEPT_HEAD/MANAGEMENT/EMPLOYEE',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门ID',
    dept_scope TEXT COMMENT '部门范围JSON数组 [101,102,103]',
    is_deleted TINYINT DEFAULT 0 COMMENT '0=正常 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '用户表';

-- 2. 操作日志表 (项目功能设计 1.4)
CREATE TABLE IF NOT EXISTS sys_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '操作人ID',
    username VARCHAR(50) COMMENT '操作人工号',
    operation VARCHAR(100) COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '操作日志';

-- 3. 数据分类表 (项目功能设计 2.1)
CREATE TABLE IF NOT EXISTS hr_data_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父ID 0=顶级',
    sort_order INT DEFAULT 0 COMMENT '排序序号'
) COMMENT '数据分类';

-- 4. 预警规则表 (开题报告 3.2.2 节)
CREATE TABLE IF NOT EXISTS warning_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_type VARCHAR(20) NOT NULL COMMENT 'FLIGHT_RISK/TALENT_SHORTAGE/COST_OVER',
    threshold FLOAT NOT NULL COMMENT '阈值',
    description VARCHAR(200) COMMENT '规则描述',
    effective TINYINT DEFAULT 0 COMMENT '0=未生效 1=生效',
    update_log TEXT COMMENT '修改历史JSON',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '预警规则';

-- 5. 部门表 (基础数据)
CREATE TABLE IF NOT EXISTS hr_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    sort_order INT DEFAULT 0
) COMMENT '部门';

-- 6. 员工档案表 (数据同步来源)
CREATE TABLE IF NOT EXISTS employee_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_no VARCHAR(50) COMMENT '员工编号',
    name VARCHAR(50) COMMENT '姓名',
    dept_id BIGINT COMMENT '部门ID',
    job VARCHAR(50) COMMENT '岗位',
    category_id BIGINT COMMENT '分类ID',
    value FLOAT COMMENT '指标值',
    period VARCHAR(20) COMMENT '统计周期',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '员工档案';

-- 7. 收藏表 (项目功能设计 5)
CREATE TABLE IF NOT EXISTS sys_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    fav_type VARCHAR(20) NOT NULL COMMENT 'REPORT/WARNING/TALENT',
    report_id BIGINT COMMENT '报表ID',
    title VARCHAR(200) COMMENT '标题',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '收藏';

-- 8. 初始化 8 大数据分类 (项目功能设计 2.1)
INSERT INTO hr_data_category (id, name, parent_id, sort_order) VALUES
(1, '组织效能分析', 0, 1),
(2, '人才梯队建设', 0, 2),
(3, '薪酬福利分析', 0, 3),
(4, '绩效管理体系', 0, 4),
(5, '员工流失预警', 0, 5),
(6, '培训效果评估', 0, 6),
(7, '人力成本优化', 0, 7),
(8, '人才发展预测', 0, 8);

-- 9. 初始化部门
INSERT INTO hr_department (id, name, parent_id, sort_order) VALUES
(101, '销售部', 0, 1),
(102, '研发部', 0, 2),
(103, '人事部', 0, 3),
(104, '财务部', 0, 4);

-- 10. 初始化预警规则 (开题报告 3.2.2)
INSERT INTO warning_rule (rule_type, threshold, description, effective) VALUES
('FLIGHT_RISK', 8.0, '员工流失率超过8%触发预警', 1),
('TALENT_SHORTAGE', 3, '关键岗位空缺超过3人触发预警', 1),
('COST_OVER', 15.0, '人力成本环比增长超过15%触发预警', 1);

-- 11. 测试用户 (密码均为 123456，BCrypt加密)
-- admin 由 DataInitializer 创建，此处补充其他角色
INSERT IGNORE INTO sys_user (username, password, role, dept_id, dept_scope, is_deleted) VALUES
('1001', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'DEPT_HEAD', 101, '[101]', 0),
('1002', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'DEPT_HEAD', 102, '[102]', 0),
('2001', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'MANAGEMENT', 103, '[101,102,103,104]', 0),
('3001', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'EMPLOYEE', 101, NULL, 0),
('3002', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'EMPLOYEE', 102, NULL, 0);

-- 12. 员工档案数据 (数据同步到 Hive 的来源，覆盖 8 大分类、4 部门)
-- 岗位：销售经理/销售员/研发工程师/产品经理/HR专员/财务等
INSERT INTO employee_profile (employee_no, name, dept_id, job, category_id, value, period, is_deleted) VALUES
('E1001','张伟',101,'销售经理',1,85.2,'202601',0),('E1002','李芳',101,'销售员',1,82.1,'202601',0),('E1003','王强',101,'销售员',2,78.5,'202601',0),
('E1004','赵敏',101,'销售经理',3,38.7,'202601',0),('E1005','刘洋',101,'销售员',4,88.0,'202601',0),('E1006','陈静',101,'销售员',5,12.5,'202601',0),
('E1007','杨磊',101,'销售员',6,89.2,'202601',0),('E1008','周婷',101,'销售员',7,35.2,'202601',0),('E1009','吴刚',101,'销售经理',8,76.5,'202601',0),
('E2001','郑浩',102,'研发工程师',1,88.5,'202601',0),('E2002','孙丽',102,'产品经理',1,86.2,'202601',0),('E2003','何涛',102,'研发工程师',2,82.0,'202601',0),
('E2004','林雪',102,'研发工程师',3,32.1,'202601',0),('E2005','高飞',102,'研发工程师',4,90.5,'202601',0),('E2006','罗敏',102,'研发工程师',5,5.2,'202601',0),
('E2007','梁杰',102,'产品经理',6,91.0,'202601',0),('E2008','宋丹',102,'研发工程师',7,30.5,'202601',0),('E2009','唐辉',102,'研发工程师',8,80.2,'202601',0),
('E3001','韩梅',103,'HR专员',1,84.0,'202601',0),('E3002','冯斌',103,'HR经理',2,79.5,'202601',0),('E3003','董娜',103,'HR专员',4,85.0,'202601',0),
('E3004','曹亮',103,'HR专员',6,92.5,'202601',0),('E4001','彭艳',104,'财务主管',1,83.5,'202601',0),('E4002','蒋鹏',104,'会计',3,40.2,'202601',0),
('E4003','谢玲',104,'出纳',7,36.8,'202601',0);

-- 2025 年历史数据（多期）
INSERT INTO employee_profile (employee_no, name, dept_id, job, category_id, value, period, is_deleted) VALUES
('E1001','张伟',101,'销售经理',1,83.5,'202501',0),('E1002','李芳',101,'销售员',1,80.2,'202501',0),('E1003','王强',101,'销售员',2,76.8,'202501',0),
('E1001','张伟',101,'销售经理',1,84.8,'202506',0),('E1002','李芳',101,'销售员',1,81.5,'202506',0),('E1001','张伟',101,'销售经理',1,85.2,'202512',0),
('E2001','郑浩',102,'研发工程师',1,86.2,'202501',0),('E2002','孙丽',102,'产品经理',1,84.5,'202501',0),('E2001','郑浩',102,'研发工程师',1,87.0,'202506',0),
('E2001','郑浩',102,'研发工程师',1,88.5,'202512',0),('E3001','韩梅',103,'HR专员',1,82.5,'202501',0),('E4001','彭艳',104,'财务主管',1,81.8,'202501',0);

-- 2024 年历史数据
INSERT INTO employee_profile (employee_no, name, dept_id, job, category_id, value, period, is_deleted) VALUES
('E1001','张伟',101,'销售经理',1,82.0,'202401',0),('E1002','李芳',101,'销售员',1,78.5,'202401',0),('E2001','郑浩',102,'研发工程师',1,85.0,'202401',0),
('E1001','张伟',101,'销售经理',1,83.2,'202406',0),('E2001','郑浩',102,'研发工程师',1,86.5,'202406',0),('E1001','张伟',101,'销售经理',1,84.0,'202412',0);

-- 13. 操作日志示例
INSERT INTO sys_log (user_id, username, operation, method, params, ip) VALUES
(1, 'admin', '用户登录', 'POST /api/auth/login', '{}', '127.0.0.1'),
(1, 'admin', '数据同步', 'POST /admin/data/sync', '{}', '127.0.0.1');
