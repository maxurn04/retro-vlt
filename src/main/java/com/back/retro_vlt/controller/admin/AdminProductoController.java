package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/productos")
@Tag(name = "Admin - Productos (CRUD & Críticos)", description = "CRUD completo y alerta de stock crítico para administradores")
public class AdminProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos para la tabla de administración")
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long categoria) {
        return ResponseEntity.ok(productoService.obtenerTodos(buscar, categoria));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto (CREATE)")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevo = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de producto para vista admin")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente (UPDATE)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizar(id, producto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (DELETE)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Producto eliminado con éxito"));
    }

    @GetMapping("/criticos")
    @Operation(summary = "Listar productos con stock crítico (stock <= stock_critico)")
    public ResponseEntity<List<Producto>> listarCriticos() {
        return ResponseEntity.ok(productoService.obtenerCriticos());
    }
}
