package com.back.retro_vlt.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtInterceptorTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtInterceptor interceptor;

    private MockHttpServletRequest peticion(String metodo, String uri, String token) {
        MockHttpServletRequest request = new MockHttpServletRequest(metodo, uri);
        if (token != null) {
            request.addHeader("Authorization", "Bearer " + token);
        }
        return request;
    }

    private void simularToken(String rol) {
        when(jwtUtil.validarToken("tok")).thenReturn(true);
        when(jwtUtil.obtenerRol("tok")).thenReturn(rol);
    }

    @Test
    void peticionOptionsPasaSinToken() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("OPTIONS", "/api/v1/admin/productos", null), response, new Object()));
    }

    @Test
    void sinTokenDevuelve401() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos", null), response, new Object()));
        assertEquals(401, response.getStatus());
    }

    @Test
    void tokenInvalidoDevuelve401() throws Exception {
        when(jwtUtil.validarToken("malo")).thenReturn(false);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos", "malo"), response, new Object()));
        assertEquals(401, response.getStatus());
    }

    @Test
    void tokenSinRolDevuelve403() throws Exception {
        simularToken(null);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    void adminAccedeATodo() throws Exception {
        simularToken("ADMIN");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("DELETE", "/api/v1/admin/productos/1", "tok"), response, new Object()));
    }

    @Test
    void vendedorPuedeVerProductos() throws Exception {
        simularToken("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos", "tok"), response, new Object()));
    }

    @Test
    void vendedorPuedeVerDetalleDeOrden() throws Exception {
        simularToken("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("GET", "/api/v1/admin/ordenes/1", "tok"), response, new Object()));
    }

    @Test
    void vendedorNoPuedeVerElDashboard() throws Exception {
        simularToken("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/dashboard/metricas", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    void vendedorNoPuedeVerProductosCriticos() throws Exception {
        simularToken("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos/criticos", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    void vendedorNoPuedeEliminarProductos() throws Exception {
        simularToken("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("DELETE", "/api/v1/admin/productos/1", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    void clienteNoPuedeAccederAlAdmin() throws Exception {
        simularToken("CLIENTE");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "/api/v1/admin/productos", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }
}