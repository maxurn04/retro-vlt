# 🎮 Guía Práctica: Cómo Iniciar el Backend Retro-VLT

Esta guía te explica **paso a paso y desde cero** cómo levantar y hacer funcionar el servidor backend en tu computador con Windows.

---

## 📌 Paso 1: Iniciar MySQL en XAMPP

Antes de ejecutar Spring Boot, la base de datos debe estar corriendo para que **Flyway** cree y cargue las tablas automáticamente:

1. Abre el **Panel de Control de XAMPP** (busca `XAMPP Control Panel` en el menú inicio de Windows).
2. En la fila de **MySQL**, haz clic en el botón **Start**.
3. Asegúrate de que el texto de MySQL se ponga de color **verde** y muestre el puerto `3306`.
4. *(Opcional)* Si quieres ver la base de datos visualmente, puedes abrir en tu navegador: [http://localhost/phpmyadmin](http://localhost/phpmyadmin) y verás la base de datos `tienda_db`.

> **Nota:** La base de datos `tienda_db` ya fue creada y configurada con usuario `root` y sin contraseña (la configuración por defecto de XAMPP).

---

## 🚀 Paso 2: Iniciar el Backend Spring Boot

Tienes **dos opciones** muy sencillas para iniciarlo:

---

### Opción A: Desde la Terminal (PowerShell o CMD) — *Recomendada*

1. Abre **Visual Studio Code** en la carpeta del proyecto (`retro-vlt`).
2. Abre una nueva terminal en VS Code con el atajo: `Ctrl + ñ` (o menú superior: **Terminal ➔ New Terminal**).
3. Si en tu sistema el comando `java` no está en el PATH global, define la ruta de Java 21 pegando esta línea:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot"; $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
   ```
4. Ejecuta el siguiente comando para compilar e iniciar el servidor:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
5. Espera unos segundos. Sabrás que inició con éxito cuando aparezcan estas líneas al final:
   ```text
   Tomcat started on port 8080 (http) with context path '/'
   Started RetroVltApplication in ... seconds
   ```

---

### Opción B: Directamente con el botón "Run" de VS Code

1. En el explorador de archivos de VS Code (a la izquierda), navega a:
   `src ➔ main ➔ java ➔ com ➔ back ➔ retro_vlt ➔ RetroVltApplication.java`
2. Abre el archivo **`RetroVltApplication.java`**.
3. Verás un texto pequeño arriba del método `main` que dice **Run | Debug**.
4. Haz clic en **Run**.
5. La consola integrada de VS Code empezará a mostrar los logs y arrancará el servidor en el puerto `8080`.

---

## 🔍 Paso 3: Verificar que Todo Está Funcionando

Una vez que el servidor esté corriendo, puedes comprobarlo de 3 formas:

### 1. En tu Navegador Web:
Abre tu navegador (Chrome, Edge, etc.) y visita:
👉 **[http://localhost:8080/api/productos](http://localhost:8080/api/productos)**

* **Resultado esperado:** Verás el JSON con la lista de videojuegos (*The Witcher 3, Minecraft, Red Dead Redemption 2, etc.*).

---

### 2. En Swagger UI (Documentación Interactiva):
Abre en tu navegador:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

* **Cómo usarlo:**
  1. Verás todos los controladores agrupados (Tienda y Admin).
  2. Haz clic sobre cualquier endpoint (por ejemplo `GET /api/productos/ofertas`).
  3. Pulsa el botón blanco **Try it out** a la derecha.
  4. Pulsa el botón azul grande **Execute**.
  5. Verás inmediatamente la respuesta del servidor con código `200 OK`.

---

### 3. En Postman:
1. Abre el programa **Postman**.
2. Haz clic en el botón **Import** (esquina superior izquierda).
3. Selecciona o arrastra el archivo **`retrovlt-postman-collection.json`** que se encuentra en la carpeta de tu proyecto.
4. Se importará la carpeta **Retro-VLT API (Tienda & Admin)** con todas las peticiones listas (Catálogo, Checkout, Pagar, Admin, etc.).
5. Selecciona cualquier petición y presiona el botón azul **Send**.

---

## 🛑 Paso 4: Cómo Detener el Servidor

Cuando quieras detener la ejecución del backend:
* Si lo iniciaste en la terminal: Presiona **`Ctrl + C`** en la terminal y confirma con **`S`** (o `Y`).
* Si lo ejecutaste con el botón Run de VS Code: Presiona el botón rojo de **Stop** (cuadrado rojo) en la barra flotante de depuración arriba.

---

## ⚠️ Solución a Problemas Frecuentes

### Problema 1: `Port 8080 was already in use`
* **Causa:** Dejaste una instancia anterior de Spring Boot abierta o hay otro programa usando el puerto 8080.
* **Solución rápida en PowerShell:**
  ```powershell
  Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force
  ```
  O si prefieres, puedes cambiar el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

### Problema 2: `Communications link failure / Connection refused` con MySQL
* **Causa:** Se te olvidó iniciar MySQL en XAMPP.
* **Solución:** Abre el Panel de XAMPP y presiona **Start** en MySQL.

### Problema 3: Error de Java al ejecutar `.\mvnw.cmd`
* **Causa:** La terminal no tiene cargada la variable de entorno de Java 21.
* **Solución:** Ejecuta esta línea en PowerShell antes de llamar a `mvnw`:
  ```powershell
  $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot"; $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
  ```
