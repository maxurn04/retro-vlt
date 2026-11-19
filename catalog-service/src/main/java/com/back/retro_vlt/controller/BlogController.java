package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Blog;
import com.back.retro_vlt.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@Tag(name = "Tienda - Blogs", description = "Endpoints de artículos de noticias y blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    @Operation(summary = "Listar todas las entradas de blog")
    public ResponseEntity<List<Blog>> listar() {
        return ResponseEntity.ok(blogService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de una entrada de blog")
    public ResponseEntity<Blog> obtenerPorId(@PathVariable Long id) {
        Blog b = blogService.obtenerPorId(id);
        if (b == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(b);
    }
}
