package com.back.retro_vlt.contact.security;

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

    private MockHttpServletRequest peticion(String metodo, String token) {
        MockHttpServletRequest request = new MockHttpServletRequest(metodo, "/api/v1/contacto");
        if (token != null) {
            request.addHeader("Authorization", "Bearer " + token);
        }
        return request;
    }

    @Test
    void enviarMensajeEsPublico() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("POST", null), response, new Object()));
    }

    @Test
    void peticionOptionsPasaSinToken() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("OPTIONS", null), response, new Object()));
    }

    @Test
    void leerMensajesSinTokenDevuelve401() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", null), response, new Object()));
        assertEquals(401, response.getStatus());
    }

    @Test
    void leerMensajesConTokenInvalidoDevuelve401() throws Exception {
        when(jwtUtil.validarToken("malo")).thenReturn(false);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "malo"), response, new Object()));
        assertEquals(401, response.getStatus());
    }

    @Test
    void vendedorNoPuedeLeerMensajes() throws Exception {
        when(jwtUtil.validarToken("tok")).thenReturn(true);
        when(jwtUtil.obtenerRol("tok")).thenReturn("VENDEDOR");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(peticion("GET", "tok"), response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    void adminPuedeLeerMensajes() throws Exception {
        when(jwtUtil.validarToken("tok")).thenReturn(true);
        when(jwtUtil.obtenerRol("tok")).thenReturn("ADMIN");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(peticion("GET", "tok"), response, new Object()));
    }
}