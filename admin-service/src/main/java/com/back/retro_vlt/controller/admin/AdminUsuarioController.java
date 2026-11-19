package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.service.OrdenService;
import com.back.retro_vlt.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Admin - Usuarios (CRUD & Historial)", description = "CRUD de usuarios e historial de compras para administradores")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario (CREATE)")
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        if (nuevo == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar el usuario. El correo ya existe o los datos son inválidos."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de usuario")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar usuario (UPDATE)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (DELETE)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Usuario eliminado con éxito"));
    }

    @GetMapping(value = {"/{id}/historial-compras", "/{id}/compras"})
    @Operation(summary = "Obtener historial de compras de un usuario específico")
    public ResponseEntity<List<Orden>> historialCompras(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorUsuario(id));
    }
}
