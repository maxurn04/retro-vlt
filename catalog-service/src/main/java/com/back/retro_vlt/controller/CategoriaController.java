package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Tienda - Categorías", description = "Endpoints para listar categorías en la tienda pública")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías disponibles")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una categoría por ID")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }
}
