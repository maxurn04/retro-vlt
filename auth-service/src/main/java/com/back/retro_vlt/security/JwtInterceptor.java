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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Acceso no autorizado: Token JWT requerido en la cabecera Authorization (Bearer <token>)\"}");
            return false;
        }

        String token = authHeader.substring(7).trim();
        if (!jwtUtil.validarToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Token JWT inválido o expirado\"}");
            return false;
        }

        String rol = jwtUtil.obtenerRol(token);
        if (rol == null || (!"ADMIN".equalsIgnoreCase(rol) && !"ADMINISTRADOR".equalsIgnoreCase(rol))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Acceso denegado: se requieren permisos de Administrador\"}");
            return false;
        }

        request.setAttribute("usuarioCorreo", jwtUtil.obtenerCorreo(token));
        request.setAttribute("usuarioRol", rol);
        return true;
    }
}
