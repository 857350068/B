-- 数据分析相关表结构
-- 依据：后端开发设计文档 五、数据库设计

-- 数据分析表
CREATE TABLE IF NOT EXISTS `data_analysis` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id` BIGINT NOT NULL COMMENT '数据分类ID',
    `analysis_name` VARCHAR(100) NOT NULL COMMENT '分析名称',
    `description` VARCHAR(500) COMMENT '分析描述',
    `analysis_type` VARCHAR(20) NOT NULL COMMENT '分析类型 (DESCRIPTIVE:描述性分析, PREDICTIVE:预测性分析, PRESCRIPTIVE:规范性分析)',
    `query_statement` TEXT NOT NULL COMMENT '查询语句 (SQL/HQL)',
    `parameters` TEXT COMMENT '参数配置 (JSON格式)',
    `result_mapping` TEXT COMMENT '结果字段映射 (JSON格式)',
    `chart_config` TEXT COMMENT '图表配置 (JSON格式)',
    `data_source` VARCHAR(10) NOT NULL COMMENT '数据源类型 (MYSQL, HIVE)',
    `execution_frequency` VARCHAR(10) COMMENT '执行频率 (ONCE:一次性, DAILY:每日, WEEKLY:每周, MONTHLY:每月)',
    `last_execution_time` DATETIME COMMENT '最后执行时间',
    `next_execution_time` DATETIME COMMENT '下次执行时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态 (ACTIVE:激活, INACTIVE:停用, RUNNING:运行中)',
    `created_by` BIGINT NOT NULL COMMENT '创建人ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识 (0:未删除, 1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_analysis_name` (`analysis_name`, `deleted`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_created_by` (`created_by`),
    KEY `idx_status` (`status`),
    KEY `idx_next_execution_time` (`next_execution_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据分析表';

-- 插入测试数据
INSERT INTO `data_analysis` (`category_id`, `analysis_name`, `description`, `analysis_type`, `query_statement`, `data_source`, `status`, `created_by`) VALUES
(1, '员工基本信息统计', '统计员工基本信息，包括年龄、性别、学历等分布情况', 'DESCRIPTIVE', 'SELECT COUNT(*) as total_count, AVG(age) as avg_age FROM employee WHERE deleted = 0', 'MYSQL', 'ACTIVE', 1),
(2, '培训效果分析', '分析培训项目的效果和参与度', 'DESCRIPTIVE', 'SELECT training_name, COUNT(*) as participant_count, AVG(score) as avg_score FROM training_record WHERE deleted = 0 GROUP BY training_name', 'MYSQL', 'ACTIVE', 1),
(3, '绩效趋势分析', '分析员工绩效的历史趋势变化', 'PREDICTIVE', 'SELECT employee_id, performance_date, performance_score FROM performance_record WHERE deleted = 0 ORDER BY performance_date', 'MYSQL', 'ACTIVE', 1),
(1, '部门人员结构分析', '分析各部门人员结构和配置情况', 'DESCRIPTIVE', 'SELECT dept_id, COUNT(*) as employee_count FROM employee WHERE deleted = 0 GROUP BY dept_id', 'MYSQL', 'ACTIVE', 1);

-- 数据分析结果表（用于存储分析结果）
CREATE TABLE IF NOT EXISTS `analysis_result` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `analysis_id` BIGINT NOT NULL COMMENT '分析任务ID',
    `result_data` LONGTEXT COMMENT '分析结果数据 (JSON格式)',
    `execution_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    `execution_duration` INT COMMENT '执行耗时(毫秒)',
    `status` VARCHAR(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '执行状态 (SUCCESS:成功, FAILED:失败)',
    `error_message` TEXT COMMENT '错误信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识 (0:未删除, 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_analysis_id` (`analysis_id`),
    KEY `idx_execution_time` (`execution_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据分析结果表';