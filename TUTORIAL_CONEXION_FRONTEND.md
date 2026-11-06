# 🔗 Tutorial: Cómo Conectar el Backend Spring Boot con el Frontend React

Esta guía explica paso a paso cómo conectar tu tienda **React** con el backend **Spring Boot (Retro-VLT)**, siguiendo exactamente la metodología de la **Guía 3.2.2** y los requerimientos de la **Evaluación 2 (DSY1104)** de Duoc UC.

---

## 🏗️ 1. Esquema de Comunicación

```
[ Frontend React (Puerto 3000 o 5173) ]
         │
         │  Peticiones HTTP (Axios / JSON)
         ▼
[ CORS Config (Spring Boot) ] ➔ Permite el origen http://localhost:3000
         │
         ▼
[ Controladores REST (/api/...) ]
         │
         ▼
[ Servicios & Repositorios JPA ]
         │
         ▼
[ Base de Datos MySQL (tienda_db) ]
```

---

## 📦 2. Preparar el Proyecto React

En la carpeta de tu proyecto React (creado con `create-react-app` o `Vite`), abre una terminal e instala **Axios** y **React Router**:

```bash
npm install axios react-router-dom
```

Dentro de la carpeta `src/` de tu frontend, organiza las siguientes carpetas:

```text
src/
├── services/          <-- Archivos que llaman a la API con Axios
│   ├── api.js         <-- Configuración base de Axios
│   ├── productoService.js
│   ├── checkoutService.js
│   ├── authService.js
│   └── adminService.js
├── components/        <-- Componentes visuales reutilizables (Navbar, Cards, etc.)
└── pages/             <-- Vistas de la tienda (Home, Detalle, Checkout, Admin, etc.)
```

---

## ⚙️ 3. Configuración Centralizada de Axios (`src/services/api.js`)

Crea el archivo `src/services/api.js` para definir la URL base del backend:

```javascript
import axios from 'axios';

// URL del backend Spring Boot
const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

export default API;
```

---

## 🛠️ 4. Servicios para Comunicarse con el Backend

Crea los servicios que encapsulan cada llamada HTTP:

### A. Servicio de Productos (`src/services/productoService.js`)
```javascript
import API from './api';

export const getProductos = (buscar = '', categoriaId = '') => {
  let url = '/productos?';
  if (buscar) url += `buscar=${encodeURIComponent(buscar)}&`;
  if (categoriaId) url += `categoria=${categoriaId}&`;
  return API.get(url);
};

export const getProductoPorId = (id) => API.get(`/productos/${id}`);

export const getOfertas = () => API.get('/productos/ofertas');

export const getCategorias = () => API.get('/categorias');
```

### B. Servicio de Compra y Checkout (`src/services/checkoutService.js`)
```javascript
import API from './api';

// Enviar los datos del formulario de despacho y los ítems del carrito
export const crearCheckout = (datos) => API.post('/checkout', datos);

// Simular el pago (éxito o rechazo)
export const procesarPago = (ordenId, simularError = false) => {
  return API.post(`/checkout/${ordenId}/pagar`, {
    metodoPago: 'TARJETA_DEBITO',
    simularError: simularError
  });
};

// Obtener los datos de la boleta por su número de orden (Ej: #20240705)
export const getBoleta = (numeroOrden) => {
  return API.get(`/ordenes/${encodeURIComponent(numeroOrden)}`);
};
```

### C. Servicio de Autenticación (`src/services/authService.js`)
```javascript
import API from './api';

export const login = (correo, password) => {
  return API.post('/auth/login', { correo, password });
};

export const registro = (datosUsuario) => {
  return API.post('/auth/registro', datosUsuario);
};
```

### D. Servicio de Administración (`src/services/adminService.js`)
```javascript
import API from './api';

export const getMetricasDashboard = () => API.get('/admin/dashboard/metricas');

export const getProductosCriticos = () => API.get('/admin/productos/criticos');

export const getAdminProductos = () => API.get('/admin/productos');

export const crearProducto = (producto) => API.post('/admin/productos', producto);

export const actualizarProducto = (id, producto) => API.put(`/admin/productos/${id}`, producto);

export const eliminarProducto = (id) => API.delete(`/admin/productos/${id}`);

export const getHistorialUsuario = (usuarioId) => API.get(`/admin/usuarios/${usuarioId}/historial-compras`);
```

---

## 💻 5. Ejemplos de Conexión en Componentes React

A continuación se muestra cómo conectar las pantallas clave descritas en el PDF de la evaluación.

---

### Ejemplo 1: Catálogo de Productos en el Home (Figura 1 y 2)

```jsx
// src/pages/Catalogo.jsx
import React, { useState, useEffect } from 'react';
import { getProductos } from '../services/productoService';
import { Link } from 'react-router-dom';

const Catalogo = () => {
  const [productos, setProductos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [buscar, setBuscar] = useState('');

  // Cargar productos al montar el componente
  useEffect(() => {
    cargarProductos();
  }, []);

  const cargarProductos = (filtro = '') => {
    setCargando(true);
    getProductos(filtro)
      .then(response => {
        setProductos(response.data);
        setCargando(false);
      })
      .catch(error => {
        console.error('Error al conectar con el backend:', error);
        setCargando(false);
      });
  };

  const handleBuscar = (e) => {
    e.preventDefault();
    cargarProductos(buscar);
  };

  if (cargando) return <p className="text-center mt-5">Cargando productos...</p>;

  return (
    <div className="container mt-4">
      <form onSubmit={handleBuscar} className="d-flex mb-4 gap-2">
        <input 
          type="text" 
          className="form-control" 
          placeholder="Buscar videojuego..."
          value={buscar}
          onChange={(e) => setBuscar(e.target.value)}
        />
        <button type="submit" className="btn btn-primary">Buscar</button>
      </form>

      <div className="row">
        {productos.map(p => (
          <div key={p.id} className="col-md-3 mb-4">
            <div className="card h-100 shadow-sm">
              <img src={p.imagenUrl} className="card-img-top" alt={p.nombre} style={{ height: '180px', objectFit: 'cover' }} />
              <div className="card-body d-flex flex-column justify-content-between">
                <div>
                  <h5 className="card-title">{p.nombre}</h5>
                  <p className="card-text text-muted small">{p.categoria ? p.categoria.nombre : ''}</p>
                  <h6 className="text-success">${p.precio.toLocaleString('es-CL')}</h6>
                </div>
                <Link to={`/productos/${p.id}`} className="btn btn-outline-primary btn-sm mt-3">
                  Ver Detalle
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Catalogo;
```

---

### Ejemplo 2: Detalle de Producto (`/productos/:id`)

```jsx
// src/pages/DetalleProducto.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getProductoPorId } from '../services/productoService';

const DetalleProducto = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState(null);

  useEffect(() => {
    getProductoPorId(id)
      .then(response => setProducto(response.data))
      .catch(error => console.error('Error al cargar producto:', error));
  }, [id]);

  if (!producto) return <p className="text-center mt-5">Cargando detalle...</p>;

  return (
    <div className="container mt-5">
      <button onClick={() => navigate(-1)} className="btn btn-secondary btn-sm mb-3">⬅ Volver</button>
      <div className="row bg-light p-4 rounded shadow-sm">
        <div className="col-md-5">
          <img src={producto.imagenUrl} alt={producto.nombre} className="img-fluid rounded" />
        </div>
        <div className="col-md-7">
          <h2>{producto.nombre}</h2>
          <p className="lead">{producto.descripcion}</p>
          <h3 className="text-success mb-3">${producto.precio.toLocaleString('es-CL')}</h3>
          <p><strong>Stock disponible:</strong> {producto.stock} unidades</p>
          <button className="btn btn-success btn-lg">Añadir al Carrito 🛒</button>
        </div>
      </div>
    </div>
  );
};

export default DetalleProducto;
```

---

### Ejemplo 3: Checkout y Simulación de Pago (Figuras 5, 6, 7 y 8)

```jsx
// src/pages/Checkout.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { crearCheckout, procesarPago } from '../services/checkoutService';

const Checkout = () => {
  const navigate = useNavigate();

  // Datos del formulario de envío
  const [cliente, setCliente] = useState({
    nombre: 'Carlos',
    apellidos: 'Mendoza',
    correo: 'carlos.mendoza@gmail.com'
  });

  const [direccion, setDireccion] = useState({
    calle: 'Av. Los Leones 1234',
    departamento: 'Depto 402',
    region: 'Región Metropolitana',
    comuna: 'Providencia',
    indicaciones: 'Dejar con el conserje'
  });

  const [simularFallo, setSimularFallo] = useState(false);
  const [procesando, setProcesando] = useState(false);

  // Ejemplo de productos en el carrito (Minecraft x4 y Witcher 3 x1)
  const items = [
    { productoId: 2, cantidad: 4 },
    { productoId: 1, cantidad: 1 }
  ];

  const handlePagar = async (e) => {
    e.preventDefault();
    setProcesando(true);

    try {
      // 1. Crear la orden en Spring Boot
      const resCheckout = await crearCheckout({ cliente, direccion, items });
      const ordenId = resCheckout.data.ordenId;

      // 2. Procesar el pago
      try {
        const resPago = await procesarPago(ordenId, simularFallo);
        // Pago exitoso (Figura 7)
        navigate(`/compra-exitosa?orden=${encodeURIComponent(resPago.data.numeroOrden)}`);
      } catch (errPago) {
        // Pago fallido (Figura 8)
        navigate(`/pago-error?orden=${encodeURIComponent(resCheckout.data.numeroOrden)}`);
      }
    } catch (error) {
      alert('Error al registrar la orden');
    } finally {
      setProcesando(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Paso de Pago / Checkout</h2>
      <form onSubmit={handlePagar} className="mt-3">
        <h5>Datos de Entrega</h5>
        <div className="mb-2">
          <label className="form-label">Calle y Número:</label>
          <input 
            type="text" 
            className="form-control" 
            value={direccion.calle} 
            onChange={e => setDireccion({...direccion, calle: e.target.value})} 
            required 
          />
        </div>
        <div className="row mb-2">
          <div className="col-md-6">
            <label className="form-label">Comuna:</label>
            <input 
              type="text" 
              className="form-control" 
              value={direccion.comuna} 
              onChange={e => setDireccion({...direccion, comuna: e.target.value})} 
              required 
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Región:</label>
            <input 
              type="text" 
              className="form-control" 
              value={direccion.region} 
              onChange={e => setDireccion({...direccion, region: e.target.value})} 
              required 
            />
          </div>
        </div>

        {/* Checkbox para probar el caso de error de la Figura 8 */}
        <div className="form-check my-3 p-3 border rounded bg-light">
          <input 
            className="form-check-input" 
            type="checkbox" 
            id="chkError" 
            checked={simularFallo} 
            onChange={e => setSimularFallo(e.target.checked)} 
          />
          <label className="form-check-label text-danger font-weight-bold" htmlFor="chkError">
            Simular pago fallido (Para probar la pantalla de "No se pudo realizar el pago")
          </label>
        </div>

        <button type="submit" disabled={procesando} className="btn btn-success btn-lg w-100">
          {procesando ? 'Procesando Pago...' : 'Pagar ahora'}
        </button>
      </form>
    </div>
  );
};

export default Checkout;
```

---

### Ejemplo 4: Panel Administrador - Dashboard y Métricas (Figura 9)

```jsx
// src/pages/admin/AdminDashboard.jsx
import React, { useState, useEffect } from 'react';
import { getMetricasDashboard } from '../../services/adminService';

const AdminDashboard = () => {
  const [metricas, setMetricas] = useState(null);

  useEffect(() => {
    getMetricasDashboard()
      .then(res => setMetricas(res.data))
      .catch(err => console.error('Error cargando métricas:', err));
  }, []);

  if (!metricas) return <p className="text-center mt-5">Cargando dashboard...</p>;

  return (
    <div className="container mt-4">
      <h2>Panel Administrativo - Dashboard</h2>
      <div className="row mt-4">
        
        <div className="col-md-4 mb-3">
          <div className="card text-white bg-primary p-3 shadow-sm">
            <h5>Total Ventas</h5>
            <h2>${metricas.ingresosTotales.toLocaleString('es-CL')}</h2>
            <p className="mb-0">{metricas.totalCompras} compras realizadas</p>
          </div>
        </div>

        <div className="col-md-4 mb-3">
          <div className="card text-white bg-success p-3 shadow-sm">
            <h5>Inventario Activo</h5>
            <h2>{metricas.productosInventario} productos</h2>
            <p className="mb-0">{metricas.stockTotalUnidades} unidades en stock</p>
          </div>
        </div>

        <div className="col-md-4 mb-3">
          <div className="card text-white bg-warning p-3 shadow-sm">
            <h5>Usuarios Registrados</h5>
            <h2>{metricas.usuariosRegistrados} clientes</h2>
            <p className="mb-0">Cuentas activas en la tienda</p>
          </div>
        </div>

        {/* Tarjeta de alerta de productos críticos */}
        <div className="col-12 mt-2">
          <div className="alert alert-danger d-flex justify-content-between align-items-center">
            <span>
              ⚠ <strong>Alerta de Inventario:</strong> Hay {metricas.productosCriticosAlerta} productos con stock crítico (bajo el umbral).
            </span>
            <a href="/admin/productos/criticos" className="btn btn-outline-danger btn-sm">Ver Productos Críticos</a>
          </div>
        </div>

      </div>
    </div>
  );
};

export default AdminDashboard;
```

---

## 🚦 6. Rutas en React (`src/App.js` o `App.jsx`)

Para conectar todas las pantallas con `react-router-dom`:

```jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Catalogo from './pages/Catalogo';
import DetalleProducto from './pages/DetalleProducto';
import Checkout from './pages/Checkout';
import AdminDashboard from './pages/admin/AdminDashboard';

function App() {
  return (
    <Router>
      <Routes>
        {/* Rutas Públicas de la Tienda */}
        <Route path="/" element={<Catalogo />} />
        <Route path="/productos/:id" element={<DetalleProducto />} />
        <Route path="/checkout" element={<Checkout />} />

        {/* Rutas del Panel Administrador */}
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
```

---

## 🛠️ 7. Preguntas Frecuentes y Solución de Problemas

### 1. ¿Qué hago si sale el error `CORS Policy: No 'Access-Control-Allow-Origin' header`?
* **Causa:** El backend no permite peticiones del puerto donde corre tu React.
* **Solución:** En el archivo `CorsConfig.java` del backend ya está configurado `http://localhost:3000` y `http://localhost:5173`. Si usas otro puerto (por ejemplo `3001`), agrégalo a la lista de `allowedOrigins` en `CorsConfig.java`.

### 2. ¿Qué hago si sale `ERR_CONNECTION_REFUSED`?
* **Causa:** El backend Spring Boot no está iniciado o se cayó.
* **Solución:** Inicia el backend ejecutando `.\mvnw.cmd spring-boot:run` en la terminal del backend.

### 3. ¿Cómo sé si el backend recibió los datos?
* Mira la terminal donde está corriendo Spring Boot. Con cada petición se imprimen los registros de consultas SQL de Hibernate (`Hibernate: select...`, `Hibernate: insert...`).
