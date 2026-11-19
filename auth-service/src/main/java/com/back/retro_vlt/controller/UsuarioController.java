package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.GoogleAuthDto;
import com.back.retro_vlt.dto.LoginDto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.security.JwtUtil;
import com.back.retro_vlt.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Tienda - Autenticación & Clientes", description = "Endpoints para registro y login de usuarios con JWT")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registro")
    @Operation(summary = "Registrar una nueva cuenta de cliente")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        if (nuevo == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar el usuario. El correo ya existe o los datos son inválidos."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con correo y contraseña generando token JWT")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos de acceso requeridos"));
        }
        Usuario usuario = usuarioService.login(dto.getCorreo(), dto.getPassword());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
        String token = jwtUtil.generarToken(usuario);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        resp.put("tipo", usuario.getRol());
        resp.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        resp.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/google")
    @Operation(summary = "Iniciar sesión o registrarse con cuenta de Google (Google Cloud)")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleAuthDto dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos de autenticación de Google requeridos"));
        }

        String googleToken = dto.getCredential() != null && !dto.getCredential().isBlank()
                ? dto.getCredential()
                : dto.getToken();

        String correo = dto.getCorreo();
        String nombre = dto.getNombre();

        if (googleToken != null && !googleToken.isBlank()) {
            Map<String, Object> payload = jwtUtil.obtenerPayload(googleToken);
            if (payload != null) {
                if (payload.containsKey("email")) {
                    correo = (String) payload.get("email");
                } else if (payload.containsKey("sub") && correo == null) {
                    correo = (String) payload.get("sub");
                }
                if (payload.containsKey("name")) {
                    nombre = (String) payload.get("name");
                }
            }
        }

        if (correo == null || correo.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo obtener el correo de la cuenta de Google"));
        }

        if (nombre == null || nombre.isBlank()) {
            nombre = "Usuario Google";
        }

        Usuario usuario = usuarioService.loginOauthGoogle(correo, nombre);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el inicio de sesión con Google"));
        }

        String token = jwtUtil.generarToken(usuario);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        resp.put("tipo", usuario.getRol());
        resp.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        resp.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        return ResponseEntity.ok(resp);
    }

    @GetMapping({"/validar", "/perfil"})
    @Operation(summary = "Validar token JWT y obtener datos del usuario autenticado")
    public ResponseEntity<?> validarSesion(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token no proporcionado"));
        }
        String token = authHeader.substring(7).trim();
        if (!jwtUtil.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o expirado"));
        }
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("valido", true);
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        return ResponseEntity.ok(resp);
    }
}
