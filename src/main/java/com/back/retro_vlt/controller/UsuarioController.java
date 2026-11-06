package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.LoginDto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Tienda - Autenticación & Clientes", description = "Endpoints para registro y login de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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
    @Operation(summary = "Iniciar sesión con correo y contraseña")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos de acceso requeridos"));
        }
        Usuario usuario = usuarioService.login(dto.getCorreo(), dto.getPassword());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "",
                "correo", usuario.getCorreo(),
                "rol", usuario.getRol(),
                "direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "",
                "telefono", usuario.getTelefono() != null ? usuario.getTelefono() : ""
        ));
    }
}
