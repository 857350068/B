-- Hive 培训效果表
-- 依据：后端开发设计文档 - 培训效果跟踪

CREATE TABLE IF NOT EXISTS hr_training_effect (
    id BIGINT,
    dept_id BIGINT,
    employee_id BIGINT,
    training_name STRING,
    score FLOAT,
    satisfaction STRING,
    create_time TIMESTAMP
)
COMMENT '培训效果数据'
STORED AS ORC;
