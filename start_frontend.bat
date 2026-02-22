@echo off
echo 正在启动人力资源数据中心系统前端服务...

cd /d "d:\B\frontend\hr-frontend"

echo 检查Node.js环境...
node -v
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js环境，请安装Node.js 16或更高版本
    pause
    exit /b 1
)

echo 检查npm环境...
npm -v
if %errorlevel% neq 0 (
    echo 错误: 未找到npm
    pause
    exit /b 1
)

echo 安装前端依赖包...
npm install

if %errorlevel% neq 0 (
    echo 警告: npm install 执行失败，可能需要手动安装依赖
)

echo 启动前端开发服务器...
start cmd /k "cd /d \"d:\B\frontend\hr-frontend\" && npm run dev"

echo.
echo 前端服务将在 http://localhost:5173 上运行
echo 请确保后端服务已启动（通常在 http://localhost:8081）
echo.
pause