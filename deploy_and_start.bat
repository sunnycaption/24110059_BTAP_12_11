@echo off
title Khoi dong Apache Tomcat 10.1.44 - Java Web MVC (JPA)
chcp 65001 > nul
echo ======================================================================
echo    DỰ ÁN JAVA WEB MVC JPA (JAKARTA EE 10 / TOMCAT 10.1 / JDK 21)
echo    Mã SV: 24110059 - Xác Thực OTP Gmail & Quản Lý Video
echo ======================================================================
echo.

set "CATALINA_HOME=C:\apache-tomcat-10.1.44"
set "JAVA_HOME=C:\Program Files\Java\jdk-21"

:: 1. Copy file WAR mới nhất vào thư mục webapps của Tomcat
echo [1/3] Đang sao chép file WAR vào Tomcat...
copy /Y "target\UserAuthProject.war" "%CATALINA_HOME%\webapps\UserAuthProject.war"

:: 2. Tự động mở trình duyệt sau 5 giây
echo [2/3] Trình duyệt sẽ tự động mở sau 5 giây tại: http://localhost:8080/UserAuthProject/login
start /B powershell -Command "Start-Sleep -Seconds 5; Start-Process 'http://localhost:8080/UserAuthProject/login'"

:: 3. Khởi động Tomcat 10.1.44
echo [3/3] Đang khởi động Apache Tomcat 10.1.44...
echo Nhấn Ctrl+C để dừng Server khi kết thúc kiểm thử.
echo ======================================================================
call "%CATALINA_HOME%\bin\catalina.bat" run
