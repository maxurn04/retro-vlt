package com.back.retro_vlt.contact.security;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "RetroVaultSecretKeyDSY1104Fullstack2026SecureJWTKey!";
    private static final Pattern ROL = Pattern.compile("\"rol\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern EXP = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    public boolean validarToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            String esperado = firmar(parts[0] + "." + parts[1]);
            if (!MessageDigest.isEqual(parts[2].getBytes(StandardCharsets.UTF_8), esperado.getBytes(StandardCharsets.UTF_8))) {
                return false;
            }
            Matcher m = EXP.matcher(payload(token));
            return m.find() && Long.parseLong(m.group(1)) * 1000 > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerRol(String token) {
        try {
            Matcher m = ROL.matcher(payload(token));
            return m.find() ? m.group(1) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String payload(String token) {
        return new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
    }

    private String firmar(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}