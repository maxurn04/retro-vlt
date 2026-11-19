# Retro-VLT Backend (Arquitectura de Microservicios)

Backend de comercio electrónico desarrollado con **Spring Boot**, **Java 21**, **Maven multi-módulo**, **MySQL** y documentación interactiva mediante **OpenAPI / Swagger**.

---

## 📋 Requisitos Previos

* **Java JDK**: Versión 21 o superior instalada y configurada (`JAVA_HOME`).
* **MySQL Server**: Versión 8.0 o superior en ejecución en el puerto `3306`.
  * Base de datos requerida: `tienda_db`
  * Si la base de datos no existe, crearla ejecutando en MySQL:
    ```sql
    CREATE DATABASE tienda_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
* **Git**: Para clonar y gestionar el repositorio.

---

## 🏗️ Estructura de Microservicios

| Microservicio | Puerto | Descripción | Documentación Swagger UI |
|---|---|---|---|
| **`common-service`** | N/A | Modelos y entidades JPA compartidos (`Usuario`, `Producto`, `Categoria`, `Orden`) y migraciones SQL. | N/A (Módulo librería) |
| **`auth-service`** | `8081` | Registro, autenticación, login Google OAuth2 y generación de tokens JWT. | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| **`catalog-service`** | `8082` | Catálogo de productos, categorías y entradas de blog de la tienda. | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| **`order-service`** | `8083` | Procesamiento de compras, pasarela de pagos simulada y consulta de boletas. | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |
| **`admin-service`** | `8084` | Panel de administración, dashboard con métricas y gestión de productos. | [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html) |
| **`contact-service`** | `8085` | Recepción y validación de mensajes del formulario de contacto. | [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html) |

---

## ⚙️ Configuración de Base de Datos

Cada microservicio cuenta con su archivo de configuración en `src/main/resources/application.properties`. Por defecto se conectan con:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda_db?useSSL=false&serverTimezone=America/Santiago&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
```

Si su instalación de MySQL utiliza una contraseña u otro usuario, actualice dichos campos en el `application.properties` correspondiente o defínalos mediante variables de entorno.

---

## 🚀 Compilación y Ejecución

### 1. Clonar el repositorio y posicionarse en la rama de microservicios

```bash
git clone https://github.com/maxurn04/retro-vlt.git
cd retro-vlt
git checkout microservicios
```

### 2. Compilar e instalar todos los módulos

El proyecto incluye el Maven Wrapper oficial. Ejecute desde la raíz del proyecto:

* **En Linux / macOS:**
  ```bash
  ./mvnw clean install
  ```
* **En Windows (CMD o PowerShell):**
  ```powershell
  .\mvnw.cmd clean install
  ```

---

### 3. Iniciar los Microservicios

Puede iniciar los microservicios en terminales independientes utilizando Maven o ejecutando directamente el archivo JAR generado:

#### Opción A: Usando Maven Wrapper (Recomendado para desarrollo)

Abra una terminal para cada servicio que desee levantar:

```bash
# Terminal 1 - Autenticación y JWT
./mvnw spring-boot:run -pl auth-service

# Terminal 2 - Catálogo y Blogs
./mvnw spring-boot:run -pl catalog-service

# Terminal 3 - Órdenes y Pagos
./mvnw spring-boot:run -pl order-service

# Terminal 4 - Administración y Métricas
./mvnw spring-boot:run -pl admin-service

# Terminal 5 - Contacto
./mvnw spring-boot:run -pl contact-service
```
*(En Windows reemplace `./mvnw` por `.\mvnw.cmd`)*

#### Opción B: Ejecutando los ejecutables JAR empaquetados

Tras ejecutar `./mvnw clean install`, se generan los ejecutables con clasificador `-exec.jar`:

```bash
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT-exec.jar
java -jar catalog-service/target/catalog-service-0.0.1-SNAPSHOT-exec.jar
java -jar order-service/target/order-service-0.0.1-SNAPSHOT-exec.jar
java -jar admin-service/target/admin-service-0.0.1-SNAPSHOT-exec.jar
java -jar contact-service/target/contact-service-0.0.1-SNAPSHOT-exec.jar
```

---

## 🧪 Ejecución de Pruebas Automatizadas

Para ejecutar toda la suite de pruebas unitarias y de integración de todos los microservicios:

```bash
./mvnw test
```
*(En Windows: `.\mvnw.cmd test`)*
