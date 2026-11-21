package com.back.retro_vlt.security;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "RetroVaultSecretKeyDSY1104Fullstack2026SecureJWTKey!";

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void tokenValidoEsAceptadoYDevuelveSuRol() throws Exception {
        String token = crearToken("ADMIN", ahora() + 3600);
        assertTrue(jwtUtil.validarToken(token));
        assertEquals("ADMIN", jwtUtil.obtenerRol(token));
    }

    @Test
    void tokenExpiradoEsRechazado() throws Exception {
        assertFalse(jwtUtil.validarToken(crearToken("ADMIN", ahora() - 3600)));
    }

    @Test
    void tokenConFirmaAlteradaEsRechazado() throws Exception {
        String token = crearToken("VENDEDOR", ahora() + 3600);
        String alterado = token.substring(0, token.length() - 1) + (token.endsWith("A") ? "B" : "A");
        assertFalse(jwtUtil.validarToken(alterado));
    }

    @Test
    void tokenConRolModificadoEsRechazado() throws Exception {
        String[] partes = crearToken("VENDEDOR", ahora() + 3600).split("\\.");
        String payloadFalso = base64("{\"sub\":\"test@test.cl\",\"rol\":\"ADMIN\",\"exp\":" + (ahora() + 3600) + "}");
        assertFalse(jwtUtil.validarToken(partes[0] + "." + payloadFalso + "." + partes[2]));
    }

    @Test
    void tokenMalFormadoONuloEsRechazado() {
        assertFalse(jwtUtil.validarToken("esto-no-es-un-jwt"));
        assertFalse(jwtUtil.validarToken("a.b"));
        assertFalse(jwtUtil.validarToken(null));
    }

    @Test
    void obtenerRolDeTokenInvalidoDevuelveNulo() {
        assertNull(jwtUtil.obtenerRol("basura"));
    }

    private long ahora() {
        return System.currentTimeMillis() / 1000;
    }

    private String crearToken(String rol, long exp) throws Exception {
        String header = base64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64("{\"sub\":\"test@test.cl\",\"rol\":\"" + rol + "\",\"exp\":" + exp + "}");
        return header + "." + payload + "." + firmar(header + "." + payload);
    }

    private String base64(String texto) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(texto.getBytes(StandardCharsets.UTF_8));
    }

    private String firmar(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}