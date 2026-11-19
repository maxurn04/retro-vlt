package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Blog;
import com.back.retro_vlt.service.BlogService;
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
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
@Tag(name = "Tienda - Blogs", description = "Endpoints de artículos de noticias, publicaciones y novedades retro")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    @Operation(
        summary = "Listar todas las entradas de blog",
        description = "Retorna el listado completo de artículos, noticias y guías publicados en el blog de Retro-VLT para visualización pública."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de entradas de blog obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Blog.class)),
                examples = @ExampleObject(
                    name = "ListadoBlogsEjemplo",
                    summary = "Ejemplo de lista con entradas de blog",
                    value = "[{\"id\":1,\"titulo\":\"Evolución y gloria de la era dorada de los 16 bits\",\"contenido\":\"En los años noventa la batalla de consolas entre SNES y Sega Genesis definió a una generación de jugadores...\",\"fechaPublicacion\":\"2024-03-15\",\"autor\":\"RetroTeam VLT\",\"imagenUrl\":\"https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=800&q=80\"},{\"id\":2,\"titulo\":\"Guía para conservar y limpiar tus cartuchos de NES\",\"contenido\":\"El mantenimiento preventivo de contactos de cobre con alcohol isopropílico al 99% asegura horas de juego sin errores...\",\"fechaPublicacion\":\"2024-03-20\",\"autor\":\"Staff Técnico\",\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=800&q=80\"}]"
                )
            )
        )
    })
    public ResponseEntity<List<Blog>> listar() {
        return ResponseEntity.ok(blogService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Ver detalle de una entrada de blog",
        description = "Obtiene los detalles completos de una entrada o artículo específico del blog mediante su ID único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Entrada de blog encontrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Blog.class),
                examples = @ExampleObject(
                    name = "DetalleBlogEjemplo",
                    summary = "Ejemplo de artículo de blog",
                    value = "{\"id\":1,\"titulo\":\"Evolución y gloria de la era dorada de los 16 bits\",\"contenido\":\"En los años noventa la batalla de consolas entre SNES y Sega Genesis definió a una generación de jugadores con franquicias inolvidables como Sonic, Mario y Donkey Kong Country.\",\"fechaPublicacion\":\"2024-03-15\",\"autor\":\"RetroTeam VLT\",\"imagenUrl\":\"https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=800&q=80\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró ninguna entrada de blog con el ID especificado",
            content = @Content
        )
    })
    public ResponseEntity<Blog> obtenerPorId(
            @Parameter(description = "Identificador único de la publicación de blog (ej: 1)", example = "1", required = true)
            @PathVariable Long id) {
        Blog b = blogService.obtenerPorId(id);
        if (b == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(b);
    }
}
