-- Hive 分析数据插入（VALUES 方式，适用于不支持 posexplode 的环境）
-- 约 2000 条数据，分批 INSERT
-- 表需先由 hr_analytic_data.sql 创建

INSERT INTO hr_analytic_data VALUES
(1,1,101,85.5,'202601',current_timestamp),(2,2,101,78.2,'202601',current_timestamp),(3,3,101,38.7,'202601',current_timestamp),
(4,4,101,88.0,'202601',current_timestamp),(5,5,101,12.5,'202601',current_timestamp),(6,6,101,89.2,'202601',current_timestamp),
(7,7,101,35.2,'202601',current_timestamp),(8,8,101,76.5,'202601',current_timestamp),(9,1,102,88.5,'202601',current_timestamp),
(10,2,102,82.0,'202601',current_timestamp),(11,3,102,32.1,'202601',current_timestamp),(12,4,102,90.5,'202601',current_timestamp),
(13,5,102,5.2,'202601',current_timestamp),(14,6,102,91.0,'202601',current_timestamp),(15,7,102,30.5,'202601',current_timestamp),
(16,8,102,80.2,'202601',current_timestamp),(17,1,103,84.0,'202601',current_timestamp),(18,2,103,79.5,'202601',current_timestamp),
(19,3,103,40.2,'202601',current_timestamp),(20,4,103,85.0,'202601',current_timestamp),(21,5,103,8.5,'202601',current_timestamp),
(22,6,103,92.5,'202601',current_timestamp),(23,7,103,36.8,'202601',current_timestamp),(24,8,103,77.0,'202601',current_timestamp),
(25,1,104,83.5,'202601',current_timestamp),(26,2,104,81.2,'202601',current_timestamp),(27,3,104,39.5,'202601',current_timestamp),
(28,4,104,86.5,'202601',current_timestamp),(29,5,104,6.8,'202601',current_timestamp),(30,6,104,88.0,'202601',current_timestamp);
