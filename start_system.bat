@echo off
echo ========================================================
echo      人力资源数据中心系统一键启动脚本
echo ========================================================
echo.
echo 本脚本将启动系统的后端和前端服务
echo.
echo 系统组成：
echo   - 后端服务：Spring Boot应用
echo   - 前端服务：Vue 3应用
echo.
echo 默认端口：
echo   - 后端：http://localhost:8081
echo   - 前端：http://localhost:5173
echo.
echo 请确保已安装以下环境：
echo   - JDK 1.8+
echo   - Maven 3.6+
echo   - Node.js 16+
echo   - MySQL 8.0 (已启动)
echo.
pause

echo.
echo 正在启动后端服务...
start cmd /k "cd /d \"d:\B\backend\" && echo 后端服务启动中... && mvn spring-boot:run -Dserver.port=8081"

timeout /t 5 /nobreak >nul

echo.
echo 正在启动前端服务...
start cmd /k "cd /d \"d:\B\frontend\hr-frontend\" && echo 安装前端依赖... && npm install && echo 前端服务启动中... && npm run dev"

echo.
echo ========================================================
echo 系统启动完成！
echo.
echo 访问地址：
echo   - 前端应用：http://localhost:5173
echo   - 后端API：http://localhost:8081
echo.
echo 默认登录账号：
echo   - 用户名：admin
echo   - 密码：123456
echo.
echo 如需停止服务，请关闭相应的命令行窗口
echo ========================================================
pause