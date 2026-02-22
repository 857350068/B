# 人力资源数据中心系统 - 部署指南

## 1. 项目概述

人力资源数据中心系统是一个基于Spring Boot + Vue 3的现代化Web应用，专注于企业人力资源数据分析、可视化展示和管理功能。系统提供了数据图表展示、收藏管理、用户管理、数据管理、规则管理和报表管理等功能模块。

## 2. 系统架构

### 2.1 技术栈

#### 后端技术栈
- **框架**: Spring Boot 2.7.17
- **持久层**: MyBatis-Plus 3.5.3.1
- **数据库**: MySQL 8.0
- **安全**: Spring Security + JWT
- **数据源**: HikariCP
- **工具库**: Lombok, Hutool

#### 前端技术栈
- **框架**: Vue 3 + TypeScript
- **UI库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **图表库**: ECharts
- **HTTP客户端**: Axios

### 2.2 项目结构

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
└── README.md               # 项目说明
```

## 3. 环境准备

### 3.1 必需软件

- **JDK 1.8+** (推荐JDK 11或更高版本)
- **Maven 3.6+**
- **Node.js 16+** (用于前端构建)
- **MySQL 8.0**
- **Git**

### 3.2 数据库配置

1. 安装并启动MySQL服务
2. 创建数据库用户和权限（可选，也可以使用root用户）

```sql
CREATE USER 'hr_user'@'%' IDENTIFIED BY 'hr_password';
GRANT ALL PRIVILEGES ON hr_db.* TO 'hr_user'@'%';
FLUSH PRIVILEGES;
```

## 4. 部署步骤

### 4.1 数据库初始化

1. 执行数据库初始化脚本：

```bash
mysql -u root -p < backend/src/main/resources/init.sql
```

2. 或者直接将init.sql内容复制到MySQL命令行执行

### 4.2 后端部署

1. 修改数据库连接配置（`backend/src/main/resources/application.yml`）：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/hr_db?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true
    username: hr_user  # 修改为实际用户名
    password: hr_password  # 修改为实际密码
```

2. 构建后端项目：

```bash
cd backend
mvn clean package -DskipTests
```

3. 运行后端应用：

```bash
java -jar target/hr-datacenter-0.0.1-SNAPSHOT.jar
```

### 4.3 前端部署

1. 安装Node.js依赖：

```bash
cd frontend/hr-frontend
npm install
```

2. 修改API基础URL（如果后端不在localhost:8080）：

编辑 `frontend/hr-frontend/src/config/api.ts`：

```typescript
export const BASE_URL = 'http://localhost:8080'; // 修改为实际后端地址
```

3. 启动前端开发服务器：

```bash
npm run dev
```

或者构建生产版本：

```bash
npm run build
```

### 4.4 Docker部署（可选）

如果有Docker环境，可以使用以下方式部署：

1. 构建后端镜像：

```bash
cd backend
docker build -t hr-backend .
```

2. 运行容器：

```bash
docker run -d -p 8080:8080 --name hr-backend hr-backend
```

## 5. 系统功能模块

### 5.1 数据图表模块

- 提供多种数据可视化图表（柱状图、折线图、饼图等）
- 支持按部门、时间维度进行数据筛选
- 实时数据展示和图表交互功能

### 5.2 收藏管理模块

- 支持收藏重要数据图表和报表
- 提供个人收藏夹管理功能
- 快捷访问常用数据视图

### 5.3 用户管理模块

- 用户增删改查功能
- 角色权限管理（管理员、部门负责人、普通用户）
- 用户状态管理

### 5.4 数据管理模块

- 数据同步到Hive功能
- Excel数据导入导出
- 数据质量检查和验证

### 5.5 规则管理模块

- 预警规则配置
- 规则启用/禁用管理
- 规则执行日志查看

### 5.6 报表管理模块

- 报表模板管理
- 自定义报表生成
- 报表数据导出功能

## 6. 默认账户

系统预置了以下测试账户：

| 用户名 | 密码 | 角色 | 部门 |
|--------|------|------|------|
| admin | 123456 | HR_ADMIN | - |
| 1001 | 123456 | DEPT_HEAD | 销售部 |
| 1002 | 123456 | DEPT_HEAD | 研发部 |
| 2001 | 123456 | MANAGEMENT | - |
| 3001 | 123456 | EMPLOYEE | 销售部 |
| 3002 | 123456 | EMPLOYEE | 研发部 |

## 7. API接口文档

主要API接口如下：

### 7.1 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息

### 7.2 图表数据接口
- `GET /api/chart/data` - 获取图表数据
- `GET /api/category/tree` - 获取数据分类树
- `GET /api/dashboard/indicators` - 获取关键指标

### 7.3 收藏接口
- `POST /api/favorite` - 添加收藏
- `GET /api/favorite` - 获取收藏列表
- `DELETE /api/favorite/{id}` - 删除收藏
- `GET /api/favorite/status` - 检查收藏状态

### 7.4 管理接口
- `GET/POST/PUT/DELETE /api/admin/users` - 用户管理
- `GET/POST/PUT/DELETE /api/admin/data` - 数据管理
- `GET/POST/PUT/DELETE /api/admin/rules` - 规则管理
- `GET/POST/PUT/DELETE /api/admin/reports` - 报表管理

## 8. 常见问题及解决方案

### 8.1 数据库连接问题

**问题**: 应用启动时报数据库连接错误
**解决方案**: 
1. 检查MySQL服务是否启动
2. 确认application.yml中的数据库连接配置是否正确
3. 验证数据库用户权限是否足够

### 8.2 前后端跨域问题

**问题**: 前端请求后端API时出现跨域错误
**解决方案**: 
- 开发环境下，已在后端配置CorsConfig允许跨域
- 生产环境下需根据实际情况调整CORS配置

### 8.3 端口占用问题

**问题**: 应用启动时报端口被占用
**解决方案**: 
- 修改application.yml中的server.port配置
- 或者停止占用端口的进程

## 9. 系统维护

### 9.1 日志管理

- 后端日志位于 `logs/` 目录下
- 日志级别可在application.yml中配置
- 包含访问日志、错误日志和业务日志

### 9.2 数据备份

定期备份数据库：

```bash
mysqldump -u hr_user -p hr_db > backup/hr_db_backup_$(date +%Y%m%d).sql
```

### 9.3 性能监控

- 定期检查系统内存和CPU使用情况
- 监控数据库连接池状态
- 关注API响应时间和并发量

## 10. 扩展建议

### 10.1 功能扩展

- 集成更多数据源（如MongoDB、Redis）
- 增加机器学习算法进行数据分析
- 添加移动端支持

### 10.2 性能优化

- 引入Redis缓存提高响应速度
- 数据库索引优化
- 前端组件懒加载

### 10.3 安全增强

- 增加验证码功能
- 实施更严格的输入验证
- 添加操作审计日志

## 11. 版权声明

本系统仅供学习和参考使用，如需商用请获得授权。