-- Hive 海量分析数据插入脚本
-- 依据：开题报告 4.2.5 节 - Hive 处理海量分析数据
-- 说明：使用 INSERT...SELECT 生成约 50,000 条数据，覆盖 4 部门、8 分类、5 年月度

-- 1. 确保表已创建（若未执行 hr_analytic_data.sql 请先执行）
-- CREATE TABLE IF NOT EXISTS hr_analytic_data ...

-- 2. 插入海量数据（通过 posexplode 生成 50000 行）
INSERT INTO hr_analytic_data
SELECT
  pe.pos AS id,
  (pe.pos % 8) + 1 AS category_id,
  101 + (pe.pos % 4) AS dept_id,
  50.0 + (pe.pos % 45) + (pe.pos % 7) * 0.5 AS value,
  concat('202', cast(floor(pe.pos / 12000) % 5 + 2 AS string), lpad(cast((pe.pos % 12) + 1 AS string), 2, '0')) AS period,
  current_timestamp AS create_time
FROM (
  SELECT posexplode(split(space(50000), ' ')) AS (pos, x)
) pe;
