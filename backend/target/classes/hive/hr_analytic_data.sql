-- Hive 分析数据表
-- 依据：开题报告 4.2.5 节、后端开发设计文档

CREATE TABLE IF NOT EXISTS hr_analytic_data (
    id BIGINT,
    category_id BIGINT,
    dept_id BIGINT,
    value FLOAT,
    period STRING,
    create_time TIMESTAMP
)
COMMENT 'HR分析数据'
STORED AS ORC;
