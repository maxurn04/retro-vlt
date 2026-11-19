package com.back.retro_vlt.security;

import com.back.retro_vlt.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerarYValidarToken() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@retrovlt.cl");
        usuario.setNombre("Test User");
        usuario.setRol("CLIENTE");

        String token = jwtUtil.generarToken(usuario);
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);

        boolean valido = jwtUtil.validarToken(token);
        assertTrue(valido);

        assertEquals("test@retrovlt.cl", jwtUtil.obtenerCorreo(token));
        assertEquals("CLIENTE", jwtUtil.obtenerRol(token));
    }

    @Test
    void testValidarTokenInvalido() {
        assertFalse(jwtUtil.validarToken(null));
        assertFalse(jwtUtil.validarToken(""));
        assertFalse(jwtUtil.validarToken("invalid.token.structure"));
        assertFalse(jwtUtil.validarToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIn0.invalidsignature"));
    }

    @Test
    void testObtenerPayload() {
        Usuario usuario = new Usuario();
        usuario.setId(42L);
        usuario.setCorreo("admin@retrovlt.cl");
        usuario.setNombre("Admin User");
        usuario.setRol("ADMIN");

        String token = jwtUtil.generarToken(usuario);
        Map<String, Object> payload = jwtUtil.obtenerPayload(token);

        assertNotNull(payload);
        assertEquals("admin@retrovlt.cl", payload.get("sub"));
        assertEquals("Admin User", payload.get("nombre"));
        assertEquals("ADMIN", payload.get("rol"));
        assertEquals(42, ((Number) payload.get("id")).intValue());
    }
}
