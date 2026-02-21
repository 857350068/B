# 登录接口测试脚本（PowerShell）
# 用法：.\scripts\test-login.ps1 或 powershell -ExecutionPolicy Bypass -File .\scripts\test-login.ps1

$url = "http://localhost:8080/api/auth/login"
$body = '{"username":"admin","password":"123456"}'

$response = Invoke-RestMethod -Uri $url -Method Post -ContentType "application/json" -Body $body
$response | ConvertTo-Json -Depth 5
