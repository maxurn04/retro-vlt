@echo off
title Iniciando Microservicios Retro-VLT
echo ============================================================
echo   Iniciando Arquitectura de Microservicios Retro-VLT
echo ============================================================
echo.
echo Asegurate de tener XAMPP con MySQL iniciado en el puerto 3306.
echo La base de datos 'tienda_db' y las tablas se crearan solas.
echo.

echo Levantando 1/5: Auth Service (Puerto 8081)...
start "Auth Service (8081)" cmd /c "mvnw.cmd spring-boot:run -pl auth-service"

echo Levantando 2/5: Catalog Service (Puerto 8082)...
start "Catalog Service (8082)" cmd /c "mvnw.cmd spring-boot:run -pl catalog-service"

echo Levantando 3/5: Order Service (Puerto 8083)...
start "Order Service (8083)" cmd /c "mvnw.cmd spring-boot:run -pl order-service"

echo Levantando 4/5: Admin Service (Puerto 8084)...
start "Admin Service (8084)" cmd /c "mvnw.cmd spring-boot:run -pl admin-service"

echo Levantando 5/5: Contact Service (Puerto 8085)...
start "Contact Service (8085)" cmd /c "mvnw.cmd spring-boot:run -pl contact-service"

echo.
echo ============================================================
echo   Todos los servicios estan iniciando en ventanas separadas!
echo ============================================================
echo - Auth:    http://localhost:8081/swagger-ui.html
echo - Catalog: http://localhost:8082/swagger-ui.html
echo - Order:   http://localhost:8083/swagger-ui.html
echo - Admin:   http://localhost:8084/swagger-ui.html
echo - Contact: http://localhost:8085/swagger-ui.html
echo ============================================================
pause
