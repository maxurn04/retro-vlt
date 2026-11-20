package com.back.retro_vlt.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return rechazar(response, HttpServletResponse.SC_UNAUTHORIZED, "Token JWT requerido (Authorization: Bearer <token>)");
        }

        String token = authHeader.substring(7).trim();
        if (!jwtUtil.validarToken(token)) {
            return rechazar(response, HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido o expirado");
        }

        String rol = jwtUtil.obtenerRol(token);
        if (rol == null) {
            return rechazar(response, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        }

        if ("ADMIN".equalsIgnoreCase(rol) || "ADMINISTRADOR".equalsIgnoreCase(rol)) {
            return true;
        }

        String path = request.getRequestURI();
        boolean lecturaPermitida = "GET".equalsIgnoreCase(request.getMethod())
                && !path.startsWith("/api/admin/productos/criticos")
                && (path.startsWith("/api/admin/productos") || path.startsWith("/api/admin/ordenes"));

        if ("VENDEDOR".equalsIgnoreCase(rol) && lecturaPermitida) {
            return true;
        }

        return rechazar(response, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: permisos insuficientes");
    }

    private boolean rechazar(HttpServletResponse response, int status, String mensaje) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + mensaje + "\"}");
        return false;
    }
}