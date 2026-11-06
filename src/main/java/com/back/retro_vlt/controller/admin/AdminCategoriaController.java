package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/categorias")
@Tag(name = "Admin - Categorías (CRUD)", description = "CRUD de categorías para el panel de administración")
public class AdminCategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría (CREATE)")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar una categoría por ID")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Mostrar una categoría por su nombre")
    public ResponseEntity<Categoria> obtenerPorNombre(@PathVariable String nombre) {
        Categoria c = categoriaService.obtenerPorNombre(nombre);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar categoría existente por ID (UPDATE)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizar(id, categoria);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/nombre/{nombre}")
    @Operation(summary = "Editar categoría existente por nombre (UPDATE)")
    public ResponseEntity<?> actualizarPorNombre(@PathVariable String nombre, @RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizarPorNombre(nombre, categoria);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID (DELETE)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Categoría eliminada con éxito"));
    }

    @DeleteMapping("/nombre/{nombre}")
    @Operation(summary = "Eliminar categoría por nombre (DELETE)")
    public ResponseEntity<?> eliminarPorNombre(@PathVariable String nombre) {
        boolean ok = categoriaService.eliminarPorNombre(nombre);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Categoría eliminada con éxito"));
    }
}
