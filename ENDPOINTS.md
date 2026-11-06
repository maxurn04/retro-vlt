# Guía Completa de Endpoints & Tutorial de Conexión (Retro-VLT API)

Bienvenido a la documentación oficial de la API REST para el proyecto **Retro-VLT (Evaluación 2 - DSY1104 Duoc UC)**.

---

## 🚀 1. Tutorial de Conexión y Ejecución

### Requisitos Previos
1. **MySQL en XAMPP**: Debe estar iniciado en el puerto estándar `3306`. La base de datos `tienda_db` se crea y puebla automáticamente mediante Flyway al arrancar.
2. **Java 21**: Instalado en el sistema.

### Cómo Iniciar el Backend
Abre una terminal en la raíz del proyecto (`retro-vlt`) y ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```
*(O en VS Code, presiona **F5** o haz clic en **Run Java** sobre `RetroVltApplication.java`).*

Una vez que veas en la consola:
```text
Tomcat started on port 8080 (http) with context path '/'
Started RetroVltApplication in ...
```
El servidor ya está disponible en: `http://localhost:8080`.

---

## 🧭 2. Cómo Probar con Swagger UI (Documentación Interactiva)

1. Con el backend corriendo, abre tu navegador web y entra a:
   👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**
2. Verás agrupados los controladores en:
   * **Tienda - Productos**: Catálogo, detalles y ofertas.
   * **Tienda - Categorías**: Lista y detalle de categorías.
   * **Tienda - Carrito & Checkout**: Registro de compras y pago simulado.
   * **Tienda - Autenticación & Clientes**: Login y registro.
   * **Tienda - Blogs**: Artículos.
   * **Admin - Dashboard**: Métricas de ventas e inventario.
   * **Admin - Productos**: CRUD de productos y alerta de stock crítico.
   * **Admin - Categorías**: CRUD de categorías.
   * **Admin - Usuarios**: CRUD de usuarios e historial de compras.
   * **Admin - Órdenes & Boletas**: Control de boletas emitidas.
3. Para probar cualquier endpoint:
   * Haz clic sobre la barra del endpoint (ej. `GET /api/productos`).
   * Haz clic en el botón **Try it out**.
   * Presiona **Execute**.
   * Podrás ver la respuesta JSON inmediata con código `200 OK`.

---

## 📮 3. Cómo Probar con Postman

### Opción A (Recomendada con 1 Clic):
Hemos incluido el archivo **`retrovlt-postman-collection.json`** en la raíz del proyecto.
1. Abre **Postman**.
2. Haz clic en el botón **Import** (arriba a la izquierda).
3. Arrastra y suelta el archivo `retrovlt-postman-collection.json` o búscalo en tu carpeta del proyecto.
4. ¡Listo! Se creará una carpeta llamada **Retro-VLT API (Tienda & Admin)** con todas las peticiones listas para enviar sin tener que tipear nada.

### Opción B (Pruebas Manuales):
* **Base URL**: `http://localhost:8080`
* **Header para peticiones POST y PUT**: `Content-Type: application/json`

---

## 📋 4. Lista Detallada de Endpoints REST

### 🛍️ A. Módulo Tienda Pública (`/api`)

#### 1. Listar Catálogo de Productos
* **Método:** `GET`
* **URL:** `/api/productos`
* **Parámetros Opcionales (Query Params):**
  * `?buscar=witcher` (filtra por nombre)
  * `?categoria=1` (filtra por ID de categoría)
* **Respuesta Exitosa (`200 OK`):**
```json
[
  {
    "id": 1,
    "nombre": "The Witcher 3: Wild Hunt",
    "descripcion": "Juego de rol y acción en mundo abierto de fantasía oscura.",
    "precio": 3999.0,
    "stock": 15,
    "stockCritico": 3,
    "esOferta": false,
    "precioOferta": null,
    "imagenUrl": "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500",
    "categoria": {
      "id": 1,
      "nombre": "Videojuegos",
      "descripcion": "Juegos para PC, PlayStation, Xbox y Nintendo",
      "imagenUrl": "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600"
    }
  }
]
```

#### 2. Detalle de un Producto
* **Método:** `GET`
* **URL:** `/api/productos/{id}` *(Ej: `/api/productos/1`)*
* **Respuesta Exitosa (`200 OK`):**
```json
{
  "id": 1,
  "nombre": "The Witcher 3: Wild Hunt",
  "descripcion": "Juego de rol y acción en mundo abierto de fantasía oscura.",
  "precio": 3999.0,
  "stock": 15,
  "stockCritico": 3,
  "esOferta": false,
  "categoria": { "id": 1, "nombre": "Videojuegos" }
}
```

#### 3. Productos en Oferta
* **Método:** `GET`
* **URL:** `/api/productos/ofertas`
* **Respuesta Exitosa (`200 OK`):**
```json
[
  {
    "id": 2,
    "nombre": "Minecraft",
    "precio": 2695.0,
    "esOferta": true,
    "precioOferta": 1990.0
  }
]
```

#### 4. Listar Categorías
* **Método:** `GET`
* **URL:** `/api/categorias`
* **Respuesta Exitosa (`200 OK`):**
```json
[
  { "id": 1, "nombre": "Videojuegos" },
  { "id": 2, "nombre": "Consolas" },
  { "id": 3, "nombre": "Accesorios" },
  { "id": 4, "nombre": "Retro" }
]
```

#### 5. Registrar Compra en Checkout (Paso de Pago - Figuras 5 y 6)
* **Método:** `POST`
* **URL:** `/api/checkout`
* **Request Body (JSON):**
```json
{
  "cliente": {
    "nombre": "Carlos",
    "apellidos": "Mendoza",
    "correo": "carlos.mendoza@gmail.com"
  },
  "direccion": {
    "calle": "Los Crisantemos, Edificio Norte",
    "departamento": "Depto 603",
    "region": "Región Metropolitana de Santiago",
    "comuna": "Cerrillos",
    "indicaciones": "Dejar con el conserje"
  },
  "items": [
    { "productoId": 1, "cantidad": 1 },
    { "productoId": 2, "cantidad": 4 }
  ]
}
```
* **Respuesta Exitosa (`201 Created`):**
```json
{
  "ordenId": 2,
  "numeroOrden": "#20261105-5384",
  "total": 9988.0,
  "estado": "PENDIENTE_PAGO",
  "mensaje": "Orden registrada con éxito. Lista para procesar pago."
}
```

#### 6. Procesar / Simular Pago (Figuras 7 y 8)
* **Método:** `POST`
* **URL:** `/api/checkout/{ordenId}/pagar` *(Ej: `/api/checkout/2/pagar`)*

**Caso A: Pago Exitoso (Figura 7 - Descuenta stock y genera boleta):**
* **Request Body:**
```json
{
  "metodoPago": "TARJETA_DEBITO",
  "simularError": false
}
```
* **Respuesta (`200 OK`):**
```json
{
  "exito": true,
  "numeroOrden": "#20261105-5384",
  "estado": "PAGADO",
  "totalPagado": 9988.0,
  "fecha": "2026-11-05T18:04:22",
  "mensaje": "Pago completado con éxito. Se ha generado su boleta."
}
```

**Caso B: Pago Rechazado (Figura 8 - Pantalla "No se pudo realizar el pago"):**
* **Request Body:**
```json
{
  "metodoPago": "TARJETA_DEBITO",
  "simularError": true
}
```
* **Respuesta (`400 Bad Request`):**
```json
{
  "exito": false,
  "numeroOrden": "#20261105-5384",
  "estado": "FALLIDO",
  "motivo": "Fondos insuficientes o transacción rechazada por el banco.",
  "accion": "VOLVER A REALIZAR EL PAGO"
}
```

#### 7. Consultar Boleta por Número de Orden
* **Método:** `GET`
* **URL:** `/api/ordenes/{numeroOrden}` *(Ej: `/api/ordenes/%2320240705` o encodeado `%23`)*
* **Respuesta Exitosa (`200 OK`):** Retorna la boleta completa con cliente, dirección, lista de productos comprados y total pagado.

#### 8. Autenticación - Inicio de Sesión
* **Método:** `POST`
* **URL:** `/api/auth/login`
* **Request Body (JSON):**
```json
{
  "correo": "carlos.mendoza@gmail.com",
  "password": "123456"
}
```
* **Respuesta Exitosa (`200 OK`):**
```json
{
  "id": 3,
  "nombre": "Carlos",
  "apellidos": "Mendoza",
  "correo": "carlos.mendoza@gmail.com",
  "rol": "CLIENTE",
  "direccionDefecto": "Av. Los Leones 1234, Depto 402, Providencia",
  "telefono": "+56987654321"
}
```

#### 9. Autenticación - Registro de Clientes
* **Método:** `POST`
* **URL:** `/api/auth/registro`
* **Request Body (JSON):**
```json
{
  "nombre": "Francisca",
  "apellidos": "Gómez",
  "correo": "francisca@example.com",
  "password": "miClaveSegura123",
  "rol": "CLIENTE",
  "telefono": "+56912345678",
  "direccionDefecto": "Pasaje Los Álamos 45"
}
```

---

### 🛠️ B. Módulo Administrador (`/api/admin`)

#### 1. Dashboard - Métricas y KPIs (Figura 9)
* **Método:** `GET`
* **URL:** `/api/admin/dashboard/metricas`
* **Respuesta Exitosa (`200 OK`):**
```json
{
  "totalCompras": 1.0,
  "ingresosTotales": 28775.0,
  "productosInventario": 8.0,
  "stockTotalUnidades": 53.0,
  "usuariosRegistrados": 2.0,
  "productosCriticosAlerta": 4.0
}
```

#### 2. CRUD Productos
* **Listar:** `GET /api/admin/productos`
* **Crear:** `POST /api/admin/productos`
  ```json
  {
    "nombre": "Dark Souls Trilogy",
    "descripcion": "Colección completa de la saga Souls.",
    "precio": 38990.0,
    "stock": 10,
    "stockCritico": 3,
    "esOferta": false,
    "imagenUrl": "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600",
    "categoria": { "id": 1 }
  }
  ```
* **Ver Detalle:** `GET /api/admin/productos/{id}`
* **Editar:** `PUT /api/admin/productos/{id}`
* **Eliminar:** `DELETE /api/admin/productos/{id}`
* **Alerta Stock Crítico:** `GET /api/admin/productos/criticos` *(devuelve los productos cuyo stock actual sea menor o igual al umbral crítico)*.

#### 3. CRUD Categorías
* **Listar:** `GET /api/admin/categorias`
* **Crear:** `POST /api/admin/categorias`
* **Ver:** `GET /api/admin/categorias/{id}`
* **Editar:** `PUT /api/admin/categorias/{id}`
* **Eliminar:** `DELETE /api/admin/categorias/{id}`

#### 4. CRUD Usuarios & Historial
* **Listar Usuarios:** `GET /api/admin/usuarios`
* **Crear Usuario:** `POST /api/admin/usuarios`
* **Ver Usuario:** `GET /api/admin/usuarios/{id}`
* **Editar Usuario:** `PUT /api/admin/usuarios/{id}`
* **Eliminar Usuario:** `DELETE /api/admin/usuarios/{id}`
* **Historial de Compras por Cliente (Figura 10):** `GET /api/admin/usuarios/{id}/historial-compras`

#### 5. Listar Órdenes / Boletas
* **Listar Todas:** `GET /api/admin/ordenes`
* **Ver Detalle de Boleta:** `GET /api/admin/ordenes/{id}`

---

## ⚡ 5. Ejemplo de Conexión desde React (Axios)

Tal como se solicita en la **Guía 3.2.2**:

```javascript
import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// Obtener productos
export const getProductos = () => API.get('/productos');

// Obtener ofertas
export const getOfertas = () => API.get('/productos/ofertas');

// Enviar orden de checkout
export const crearOrden = (datosCheckout) => API.post('/checkout', datosCheckout);

// Pagar orden
export const pagarOrden = (ordenId, datosPago) => API.post(`/checkout/${ordenId}/pagar`, datosPago);
```
