package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/categorias")
@Tag(name = "Admin - Categorías (CRUD)", description = "Operaciones CRUD para la gestión de categorías en el panel de administración")
public class AdminCategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Retorna el listado completo de categorías disponibles en el catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de categorías obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Categoria.class)),
                            examples = @ExampleObject(name = "Lista de Categorias", value = "[\n  {\n    \"id\": 1,\n    \"nombre\": \"Videojuegos\",\n    \"descripcion\": \"Juegos para PC, PlayStation, Xbox y Nintendo\",\n    \"imagenUrl\": \"https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600\"\n  },\n  {\n    \"id\": 2,\n    \"nombre\": \"Consolas\",\n    \"descripcion\": \"Consolas de nueva generación y ediciones retro\",\n    \"imagenUrl\": \"https://images.unsplash.com/photo-1486401899868-0e435ed85128?w=600\"\n  }\n]")))
    })
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría (CREATE)", description = "Crea y registra una nueva categoría para organizar productos en la tienda.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Categoria Creada", value = "{\n  \"id\": 5,\n  \"nombre\": \"Arcade & Flipper\",\n  \"descripcion\": \"Máquinas recreativas retro, bartops y placas multijuegos clásicas.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1511512578047-dfb367046420?w=600\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Categoria> crear(
            @RequestBody(description = "Datos de la nueva categoría a registrar", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Ejemplo Crear Categoria", value = "{\n  \"nombre\": \"Arcade & Flipper\",\n  \"descripcion\": \"Máquinas recreativas retro, bartops y placas multijuegos clásicas.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1511512578047-dfb367046420?w=600\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar una categoría por ID", description = "Busca y retorna los datos detallados de una categoría según su ID numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Categoria Encontrada", value = "{\n  \"id\": 1,\n  \"nombre\": \"Videojuegos\",\n  \"descripcion\": \"Juegos para PC, PlayStation, Xbox y Nintendo\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada con el ID proporcionado")
    })
    public ResponseEntity<Categoria> obtenerPorId(
            @Parameter(description = "ID numérico de la categoría a consultar", example = "1", required = true)
            @PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Mostrar una categoría por su nombre", description = "Busca y retorna una categoría utilizando su nombre (búsqueda sin distinción de mayúsculas).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Categoria Encontrada", value = "{\n  \"id\": 1,\n  \"nombre\": \"Videojuegos\",\n  \"descripcion\": \"Juegos para PC, PlayStation, Xbox y Nintendo\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<Categoria> obtenerPorNombre(
            @Parameter(description = "Nombre de la categoría a buscar", example = "Videojuegos", required = true)
            @PathVariable String nombre) {
        Categoria c = categoriaService.obtenerPorNombre(nombre);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar categoría existente por ID (UPDATE)", description = "Actualiza los campos de una categoría existente localizándola por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Categoria Actualizada", value = "{\n  \"id\": 1,\n  \"nombre\": \"Videojuegos y Software\",\n  \"descripcion\": \"Juegos físicos y digitales para PC, PlayStation, Xbox y consolas Nintendo.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada para actualizar")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID numérico de la categoría a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Datos actualizados de la categoría", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Ejemplo Actualizar Categoria", value = "{\n  \"nombre\": \"Videojuegos y Software\",\n  \"descripcion\": \"Juegos físicos y digitales para PC, PlayStation, Xbox y consolas Nintendo.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizar(id, categoria);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/nombre/{nombre}")
    @Operation(summary = "Editar categoría existente por nombre (UPDATE)", description = "Actualiza los campos de una categoría localizándola por su nombre actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Categoria Actualizada por Nombre", value = "{\n  \"id\": 2,\n  \"nombre\": \"Consolas y Hardware\",\n  \"descripcion\": \"Consolas de última generación y modelos retro restaurados.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1486401899868-0e435ed85128?w=600\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> actualizarPorNombre(
            @Parameter(description = "Nombre actual de la categoría a actualizar", example = "Consolas", required = true)
            @PathVariable String nombre,
            @RequestBody(description = "Datos actualizados de la categoría", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(name = "Ejemplo Actualizar por Nombre", value = "{\n  \"nombre\": \"Consolas y Hardware\",\n  \"descripcion\": \"Consolas de última generación y modelos retro restaurados.\",\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1486401899868-0e435ed85128?w=600\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizarPorNombre(nombre, categoria);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID (DELETE)", description = "Elimina permanentemente una categoría de la base de datos según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Categoría eliminada con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID numérico de la categoría a eliminar", example = "4", required = true)
            @PathVariable Long id) {
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Categoría eliminada con éxito"));
    }

    @DeleteMapping("/nombre/{nombre}")
    @Operation(summary = "Eliminar categoría por nombre (DELETE)", description = "Elimina permanentemente una categoría de la base de datos por su nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación por Nombre", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Categoría eliminada con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> eliminarPorNombre(
            @Parameter(description = "Nombre de la categoría a eliminar", example = "Retro", required = true)
            @PathVariable String nombre) {
        boolean ok = categoriaService.eliminarPorNombre(nombre);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Categoría eliminada con éxito"));
    }
}
