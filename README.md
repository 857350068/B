# 人力资源数据中心系统

## 项目概述

本项目是一个功能完整的人力资源数据中心系统，采用前后端分离架构，旨在为企业提供人力资源数据的采集、分析、可视化展示和管理功能。系统涵盖从数据图表展示到后台管理的全方位功能模块。

## 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.7.17
- **持久层**: MyBatis-Plus 3.5.3.1 + MySQL 8.0
- **安全**: Spring Security + JWT Token认证
- **工具**: Lombok、Hutool、MyBatis-Plus Generator
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue 3 + TypeScript
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **数据可视化**: ECharts
- **HTTP客户端**: Axios

## 功能模块

### 1. 数据图表模块
- 提供多种数据可视化图表（柱状图、折线图、饼图等）
- 支持按部门、时间维度进行数据筛选
- 实时数据展示和图表交互功能

### 2. 收藏管理模块
- 支持收藏重要数据图表和报表
- 提供个人收藏夹管理功能
- 快捷访问常用数据视图

### 3. 用户管理模块
- 用户增删改查功能
- 角色权限管理（管理员、部门负责人、普通用户）
- 用户状态管理

### 4. 数据管理模块
- 数据同步到Hive功能
- Excel数据导入导出
- 数据质量检查和验证

### 5. 规则管理模块
- 预警规则配置
- 规则启用/禁用管理
- 规则执行日志查看

### 6. 报表管理模块
- 报表模板管理
- 自定义报表生成
- 报表数据导出功能

## 环境要求

### 1. 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0
- Node.js 16+ （前端）

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS hr_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hr_db;

-- 执行初始化脚本
source backend/src/main/resources/init.sql;
```

### 3. 配置文件修改

修改 `backend/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/hr_db?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: your_username  # 替换为实际用户名
    password: your_password  # 替换为实际密码
```

修改 `frontend/hr-frontend/src/config/api.ts` 中的API基础URL（如后端端口不是8080）：

```typescript
export const BASE_URL = 'http://localhost:8081'; // 根据实际后端端口调整
```

## 快速启动

### 方法一：使用一键启动脚本（推荐）

双击运行 `start_system.bat` 文件，系统将自动启动后端和前端服务。

### 方法二：手动启动

#### 1. 启动后端服务

```bash
cd backend
mvn spring-boot:run -Dserver.port=8081
```

#### 2. 启动前端服务

```bash
cd frontend/hr-frontend
npm install
npm run dev
```

## 访问系统

- 前端应用：http://localhost:5173
- 后端API：http://localhost:8081

## 默认账户

| 用户名 | 密码 | 角色 | 部门 |
|--------|------|------|------|
| admin | 123456 | HR_ADMIN | - |
| 1001 | 123456 | DEPT_HEAD | 销售部 |
| 1002 | 123456 | DEPT_HEAD | 研发部 |
| 2001 | 123456 | MANAGEMENT | - |
| 3001 | 123456 | EMPLOYEE | 销售部 |
| 3002 | 123456 | EMPLOYEE | 研发部 |

## 部署指南

详细部署指南请参阅 `deployment_guide.md` 文件。

## 项目总结

完整的项目总结请参阅 `project_summary.md` 文件。

## 项目结构

```
d:\B\
├── backend/                 # 后端Spring Boot项目
│   ├── src/main/java/com/hr/
│   │   ├── common/         # 通用类
│   │   ├── config/         # 配置类
│   │   ├── controller/     # 控制器
│   │   ├── exception/      # 异常处理
│   │   ├── mapper/         # 数据访问层
│   │   ├── model/          # 数据模型
│   │   ├── security/       # 安全相关
│   │   ├── service/        # 业务逻辑层
│   │   └── util/           # 工具类
│   ├── src/main/resources/
│   │   ├── mapper/         # MyBatis映射文件
│   │   ├── application.yml # 配置文件
│   │   └── init.sql        # 数据库初始化脚本
│   └── pom.xml             # Maven依赖配置
├── frontend/hr-frontend/   # 前端Vue项目
│   ├── src/
│   │   ├── api/            # API接口定义
│   │   ├── components/     # 通用组件
│   │   ├── views/          # 页面组件
│   │   ├── router/         # 路由配置
│   │   ├── stores/         # Pinia状态管理
│   │   └── utils/          # 工具函数
│   └── package.json        # NPM依赖配置
├── doc/                    # 文档
├── scripts/                # 脚本文件
├── deployment_guide.md     # 部署指南
├── project_summary.md      # 项目总结
├── start_backend.bat       # 后端启动脚本
├── start_frontend.bat      # 前端启动脚本
├── start_system.bat        # 一键启动脚本
└── README.md               # 项目说明
```
