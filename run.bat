@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.21.9-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

set "JAR_PATH=%~dp0backend\target\serviceflow-backend-1.0.0.jar"

if not exist "%JAR_PATH%" (
    echo Jar not found. Building project...
    cd /d "%~dp0backend"
    call mvn clean package -DskipTests
    cd /d "%~dp0"
)

echo ========================================================
echo Starting ServiceFlow Backend
echo ========================================================
echo Swagger UI:    http://localhost:8080/swagger-ui.html
echo H2 Console:    http://localhost:8080/h2-console
echo Health Check:  http://localhost:8080/api/health
echo ========================================================

java -jar "%JAR_PATH%"
