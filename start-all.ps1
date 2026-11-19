# Script para iniciar todos los microservicios de Retro-VLT en terminales separadas
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Iniciando Microservicios Retro-VLT (XAMPP MySQL + Flyway)  " -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Verificando conexion a MySQL (localhost:3306)..." -ForegroundColor Yellow

$test = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue
if ($test.TcpTestSucceeded) {
    Write-Host "MySQL detectado activo en el puerto 3306." -ForegroundColor Green
} else {
    Write-Host "ADVERTENCIA: No se detecto MySQL en localhost:3306." -ForegroundColor Red
    Write-Host "Por favor inicia el modulo MySQL en el Panel de Control de XAMPP." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Levantando microservicios..." -ForegroundColor Cyan

$services = @(
    @{ Name = "Auth Service"; Module = "auth-service"; Port = 8081 },
    @{ Name = "Catalog Service"; Module = "catalog-service"; Port = 8082 },
    @{ Name = "Order Service"; Module = "order-service"; Port = 8083 },
    @{ Name = "Admin Service"; Module = "admin-service"; Port = 8084 },
    @{ Name = "Contact Service"; Module = "contact-service"; Port = 8085 }
)

foreach ($s in $services) {
    Write-Host " -> Iniciando $($s.Name) en puerto $($s.Port)..." -ForegroundColor Green
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Iniciando $($s.Name)...' -ForegroundColor Cyan; .\mvnw.cmd spring-boot:run -pl $($s.Module)"
}

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Todos los microservicios han sido lanzados!" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "- Auth Service:    http://localhost:8081/swagger-ui.html"
Write-Host "- Catalog Service: http://localhost:8082/swagger-ui.html"
Write-Host "- Order Service:   http://localhost:8083/swagger-ui.html"
Write-Host "- Admin Service:   http://localhost:8084/swagger-ui.html"
Write-Host "- Contact Service: http://localhost:8085/swagger-ui.html"
Write-Host "============================================================" -ForegroundColor Cyan
