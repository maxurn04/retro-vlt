# Retro-VLT Backend (Arquitectura de Microservicios)

Backend de comercio electrónico desarrollado con **Spring Boot**, **Java 21**, **Maven multi-módulo**, **MySQL (XAMPP)**, migraciones automáticas con **Flyway** y documentación interactiva mediante **OpenAPI / Swagger**.

---

## 📋 Requisitos Previos

* **Java JDK**: Versión 21 o superior instalada y configurada (`JAVA_HOME`).
* **XAMPP**: Iniciar el servicio **MySQL** desde el Panel de Control de XAMPP (puerto por defecto `3306`).
* **Base de datos automática**: **No es necesario crear la base de datos manualmente en phpMyAdmin**. El backend incluye `createDatabaseIfNotExist=true` y migraciones automáticas con **Flyway** (`V1` a `V4`). Al levantar cualquier servicio, la base de datos `tienda_db`, todas las tablas y los datos iniciales (usuarios, productos, categorías, blogs, órdenes) se crean solos automáticamente.

---

## 🏗️ Mapa de Microservicios y Puertos

| Microservicio | Puerto | Descripción | Documentación Swagger UI |
|---|---|---|---|
| **`ms-autenticacion`** | `8081` | Registro, autenticación, login Google OAuth2 y generación de tokens JWT. | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| **`ms-catalogo`** | `8082` | Catálogo de productos, categorías y artículos de blog de la tienda. | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| **`ms-ordenes`** | `8083` | Procesamiento de compras, pasarela de pagos simulada y consulta de boletas. | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |
| **`ms-administracion`** | `8084` | Panel de administración, dashboard con métricas y gestión de productos. | [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html) |
| **`ms-contacto`** | `8085` | Recepción y validación de mensajes del formulario de contacto (stateless). | [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html) |

---

## ⚙️ Configuración con XAMPP

Cada microservicio con persistencia está configurado para conectarse directamente a XAMPP en su archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Santiago&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate y Flyway
spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

---

## 🚀 Cómo Iniciar el Backend

### 1. Iniciar MySQL en XAMPP
Abra el **XAMPP Control Panel** y haga clic en **Start** en el módulo **MySQL**.

---

### 2. Opción A: Usando Spring Boot Dashboard / Manager en VS Code (Recomendado)

Si utiliza Visual Studio Code con la extensión **Spring Boot Dashboard** (`vscjava.vscode-spring-boot-dashboard`):

1. Abra la pestaña **Spring Boot Dashboard** en la barra lateral izquierda (ícono de hoja verde 🍃).
2. Verá listados los 5 microservicios (`ms-autenticacion`, `ms-catalogo`, `ms-ordenes`, `ms-administracion`, `ms-contacto`).
3. Puede hacer clic en el botón **Play ▶️** de cada servicio o en la parte superior para levantarlos.
4. **O alternativamente**: Vaya al menú **Ejecutar y depurar** (`Ctrl + Shift + D` o `F5`) y seleccione:
   `🚀 Iniciar Todos los Microservicios (Spring Boot Manager)`. Esto iniciará todos los microservicios simultáneamente.

---

### 3. Opción B: Script de 1 Clic (Windows)

Haga doble clic en el archivo [start-all.bat](file:///e:/Documentos/retro-backend/start-all.bat) o ejecute en PowerShell:

```powershell
.\start-all.ps1
```

Esto abrirá terminales independientes levantando cada uno de los microservicios de manera ordenada.

---

### 4. Opción C: Usando Maven Wrapper desde la Terminal

```bash
# Terminal 1 - Autenticación y JWT (Puerto 8081)
./mvnw spring-boot:run -pl ms-autenticacion

# Terminal 2 - Catálogo y Blogs (Puerto 8082)
./mvnw spring-boot:run -pl ms-catalogo

# Terminal 3 - Órdenes y Pagos (Puerto 8083)
./mvnw spring-boot:run -pl ms-ordenes

# Terminal 4 - Administración y Métricas (Puerto 8084)
./mvnw spring-boot:run -pl ms-administracion

# Terminal 5 - Contacto (Puerto 8085)
./mvnw spring-boot:run -pl ms-contacto
```
*(En Windows reemplace `./mvnw` por `.\mvnw.cmd`)*

---

## 🧪 Pruebas Automatizadas

Para validar que todos los servicios y entidades compilen y pasen sus suites de prueba unitarias:

```bash
./mvnw test
```
*(En Windows: `.\mvnw.cmd test`)*
