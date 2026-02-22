@echo off
echo 正在启动人力资源数据中心系统后端服务...

cd /d "d:\B\backend"

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装JDK 1.8或更高版本
    pause
    exit /b 1
)

echo 检查Maven环境...
mvn -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 启动后端服务...
echo 请注意：如果端口8080已被占用，系统将自动尝试其他端口
start cmd /k "cd /d \"d:\B\backend\" && mvn spring-boot:run -Dserver.port=8081"

echo.
echo 后端服务将在 http://localhost:8081 上运行
echo 请等待几分钟直到服务完全启动后再访问
echo.
pause