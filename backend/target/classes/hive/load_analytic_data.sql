-- Hive 海量数据加载方案（备选）
-- 当 insert_analytic_data.sql 的 INSERT...SELECT 因 Hive 版本不支持时使用
-- 步骤：1. 使用 scripts/generate_hive_data.py 生成 data.csv  2. 执行本脚本

USE default;

-- 创建临时文本表用于加载 CSV
CREATE TABLE IF NOT EXISTS hr_analytic_data_staging (
    id BIGINT,
    category_id BIGINT,
    dept_id BIGINT,
    value FLOAT,
    period STRING,
    create_time STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
TBLPROPERTIES ('skip.header.line.count'='1');

-- 加载 CSV（需先将 data.csv 上传到 HDFS 或使用 LOCAL）
-- LOAD DATA LOCAL INPATH '/path/to/analytic_data.csv' INTO TABLE hr_analytic_data_staging;

-- 从临时表插入目标 ORC 表
-- INSERT INTO hr_analytic_data SELECT id, category_id, dept_id, value, period, cast(create_time as timestamp) FROM hr_analytic_data_staging;
