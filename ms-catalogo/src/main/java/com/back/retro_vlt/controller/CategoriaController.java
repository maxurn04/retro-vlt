package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@Tag(name = "Tienda - Categorías", description = "Endpoints para listar categorías en la tienda pública")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(
        summary = "Listar todas las categorías disponibles",
        description = "Obtiene el catálogo completo de categorías activas para clasificar productos en la tienda pública Retro-VLT."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Categoria.class)),
                examples = @ExampleObject(
                    name = "ListadoCategoriasEjemplo",
                    summary = "Ejemplo de listado de categorías",
                    value = "[{\"id\":1,\"nombre\":\"Consolas\",\"descripcion\":\"Consolas retro clásicas de 8, 16 y 32 bits originales y restauradas\",\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80\"},{\"id\":2,\"nombre\":\"Juegos\",\"descripcion\":\"Cartuchos y discos originales para consolas retro clásicas\",\"imagenUrl\":\"https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&w=600&q=80\"},{\"id\":3,\"nombre\":\"Accesorios\",\"descripcion\":\"Mandos, cables, fuentes de poder y periféricos de época\",\"imagenUrl\":\"https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=600&q=80\"}]"
                )
            )
        )
    })
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener detalle de una categoría por ID",
        description = "Consulta y recupera los datos descriptivos de una categoría por su identificador numérico único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Categoria.class),
                examples = @ExampleObject(
                    name = "DetalleCategoriaEjemplo",
                    summary = "Ejemplo de detalle de categoría",
                    value = "{\"id\":1,\"nombre\":\"Consolas\",\"descripcion\":\"Consolas retro clásicas de 8, 16 y 32 bits originales y restauradas\",\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró ninguna categoría con el ID indicado",
            content = @Content
        )
    })
    public ResponseEntity<Categoria> obtenerPorId(
            @Parameter(description = "Identificador numérico de la categoría a consultar (ej: 1)", example = "1", required = true)
            @PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }
}
