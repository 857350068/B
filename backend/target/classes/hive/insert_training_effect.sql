-- Hive 培训效果数据插入（约 5000 条）
INSERT INTO hr_training_effect
SELECT
  pe.pos AS id,
  101 + (pe.pos % 4) AS dept_id,
  1000 + (pe.pos % 500) AS employee_id,
  CASE (pe.pos % 10)
    WHEN 0 THEN '销售技能提升培训'
    WHEN 1 THEN '管理能力进阶培训'
    WHEN 2 THEN '技术能力培训'
    WHEN 3 THEN '新员工入职培训'
    WHEN 4 THEN '领导力发展培训'
    WHEN 5 THEN '绩效管理培训'
    ELSE '综合能力培训'
  END AS training_name,
  70.0 + (pe.pos % 25) + (pe.pos % 5) * 0.5 AS score,
  concat(85 + (pe.pos % 15), '%') AS satisfaction,
  current_timestamp AS create_time
FROM (SELECT posexplode(split(space(5000), ' ')) AS (pos, x)) pe;
