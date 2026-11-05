package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Tienda - Productos", description = "Endpoints para el catálogo de la tienda pública")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar catálogo de productos (con filtro de búsqueda o categoría)")
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long categoria) {
        return ResponseEntity.ok(productoService.obtenerTodos(buscar, categoria));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un producto por ID")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/ofertas")
    @Operation(summary = "Obtener todos los productos en oferta")
    public ResponseEntity<List<Producto>> listarOfertas() {
        return ResponseEntity.ok(productoService.obtenerOfertas());
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar productos de una categoría específica")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.obtenerTodos(null, categoriaId));
    }
}
