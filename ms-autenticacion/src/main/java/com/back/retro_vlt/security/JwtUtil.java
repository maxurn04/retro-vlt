package com.back.retro_vlt.security;

import com.back.retro_vlt.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "RetroVaultSecretKeyDSY1104Fullstack2026SecureJWTKey!";
    private static final long EXPIRATION_MS = 86400000L;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generarToken(Usuario usuario) {
        try {
            long now = System.currentTimeMillis();
            long exp = now + EXPIRATION_MS;

            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", usuario.getCorreo());
            payload.put("id", usuario.getId());
            payload.put("nombre", usuario.getNombre());
            payload.put("rol", usuario.getRol());
            payload.put("tipo", usuario.getRol());
            payload.put("iat", now / 1000);
            payload.put("exp", exp / 1000);

            String headerEncoded = base64UrlEncode(objectMapper.writeValueAsString(header).getBytes(StandardCharsets.UTF_8));
            String payloadEncoded = base64UrlEncode(objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8));
            String dataToSign = headerEncoded + "." + payloadEncoded;

            String signature = sign(dataToSign, SECRET_KEY);
            return dataToSign + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar token JWT", e);
        }
    }

    public boolean validarToken(String token) {
        try {
            if (token == null || !token.contains(".")) {
                return false;
            }
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            String dataToSign = parts[0] + "." + parts[1];
            String expectedSignature = sign(dataToSign, SECRET_KEY);
            if (!MessageDigest.isEqual(parts[2].getBytes(StandardCharsets.UTF_8), expectedSignature.getBytes(StandardCharsets.UTF_8))) {
                return false;
            }
            Map<String, Object> payload = obtenerPayload(token);
            if (payload == null) {
                return false;
            }
            Number exp = (Number) payload.get("exp");
            if (exp == null) {
                return false;
            }
            return (exp.longValue() * 1000) > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
            return objectMapper.readValue(bytes, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String obtenerCorreo(String token) {
        Map<String, Object> payload = obtenerPayload(token);
        return payload != null ? (String) payload.get("sub") : null;
    }

    public String obtenerRol(String token) {
        Map<String, Object> payload = obtenerPayload(token);
        return payload != null ? (String) payload.get("rol") : null;
    }

    private String sign(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(hash);
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
