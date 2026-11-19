package com.back.retro_vlt.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", LocalDateTime.now());
        resp.put("status", HttpStatus.BAD_REQUEST.value());
        resp.put("error", "Solicitud inválida");
        resp.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", LocalDateTime.now());
        resp.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        resp.put("error", "Error interno del servidor");
        resp.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}
