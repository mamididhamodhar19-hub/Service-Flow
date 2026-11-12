$ErrorActionPreference = "Stop"

$Java17 = "C:\Program Files\Eclipse Adoptium\jdk-17.0.21.9-hotspot"
if (Test-Path $Java17) {
    $env:JAVA_HOME = $Java17
    $env:PATH = "$Java17\bin;$env:PATH"
}

$JarPath = Join-Path $PSScriptRoot "backend\target\serviceflow-backend-1.0.0.jar"
if (-not (Test-Path $JarPath)) {
    Write-Host "Jar not found. Building project..." -ForegroundColor Yellow
    Push-Location (Join-Path $PSScriptRoot "backend")
    mvn clean package -DskipTests
    Pop-Location
}

Write-Host "========================================================" -ForegroundColor Green
Write-Host " Starting ServiceFlow Backend" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
Write-Host "Swagger UI:    http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "H2 Console:    http://localhost:8080/h2-console" -ForegroundColor Cyan
Write-Host "Health Check:  http://localhost:8080/api/health" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Green

& java -jar $JarPath
