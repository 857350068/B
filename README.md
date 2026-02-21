# 人力资源数据中心系统

> 依据 doc/ 目录下技术文档完整开发
> SpringBoot 2.7.17 + MyBatis + Vue 3 + Element Plus

## 快速开始

### 1. 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0
- Node.js 16+ （前端）

### 2. 数据库初始化

```sql
-- 创建数据库和用户
CREATE DATABASE hr_db DEFAULT CHARACTER SET utf8mb4;
CREATE USER 'hr_user'@'%' IDENTIFIED BY 'hr_password';
GRANT ALL ON hr_db.* TO 'hr_user'@'%';
FLUSH PRIVILEGES;

-- 执行 backend/src/main/resources/init.sql
USE hr_db;
SOURCE backend/src/main/resources/init.sql;
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

服务启动后：
- 端口：8080
- 管理员账号：admin / 123456（首次启动自动创建）
- 登录接口：POST http://localhost:8080/api/auth/login

### 4. 验证接口

**Windows PowerShell（必须用 curl.exe，JSON 用双引号+转义）：**

```powershell
# 登录（PowerShell 中 -d 用双引号包裹，内部 \" 转义）
curl.exe -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"123456\"}"

# 获取分类（需携带返回的 token）
curl.exe http://localhost:8080/api/category/tree -H "Authorization: Bearer <你的token>"

# 获取预警
curl.exe http://localhost:8080/api/warning -H "Authorization: Bearer <你的token>"
```

**Linux / Git Bash：**

```bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"123456"}'
```

**推荐**：用 Postman 发送 POST，Body 选 raw + JSON，内容：`{"username":"admin","password":"123456"}`

或直接运行：`powershell -ExecutionPolicy Bypass -File scripts/test-login.ps1`

## 数据说明

### MySQL 数据（init.sql）
- 用户：admin（自动创建）、1001/1002（部门负责人）、2001（管理层）、3001/3002（员工），密码均为 123456
- 部门：101 销售部、102 研发部、103 人事部、104 财务部
- 8 大分类、预警规则、员工档案（含 2024-2026 历史数据）、操作日志

### Hive 海量数据
- `hive/hr_analytic_data.sql`：建表
- `hive/insert_analytic_data.sql`：INSERT SELECT 生成约 5 万条（4 部门 × 8 分类 × 5 年月度）
- `hive/insert_analytic_data_values.sql`：VALUES 方式约 30 条（备用）
- `scripts/generate_hive_data.py`：生成 CSV，可配合 `load_analytic_data.sql` 批量导入

```bash
# 生成 5 万行 CSV
python scripts/generate_hive_data.py 50000
```

## 项目结构

```
D:\B\
├── backend/           # SpringBoot 后端
├── frontend/          # Vue 3 前端（待开发）
├── doc/               # 技术文档
├── scripts/           # 数据生成脚本
└── deploy/            # 部署脚本（待补充）
```

## 开发进度

- [x] 阶段0：环境与基础
- [x] 阶段1：用户认证（登录、JWT、RBAC）
- [x] 阶段2：数据分类、看板
- [x] 阶段3：预警信息
- [ ] 阶段4-9：进行中

详见 `doc/完整开发计划.md`
