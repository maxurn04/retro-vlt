package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.service.ProductoService;
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
@RequestMapping("/api/v1/admin/productos")
@Tag(name = "Admin - Productos (CRUD & Críticos)", description = "CRUD completo y gestión de inventario/alertas de stock para administradores")
public class AdminProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Retorna el catálogo completo para el panel administrativo, con filtros opcionales por texto (nombre) y por categoría.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo de productos obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                            examples = @ExampleObject(name = "Lista de Productos", value = "[\n  {\n    \"id\": 1,\n    \"codigo\": \"PROD-W3\",\n    \"nombre\": \"The Witcher 3: Wild Hunt\",\n    \"descripcion\": \"Juego de rol y acción en mundo abierto de fantasía oscura.\",\n    \"precio\": 3999.0,\n    \"stock\": 15,\n    \"stockCritico\": 3,\n    \"esOferta\": false,\n    \"precioOferta\": null,\n    \"imagenUrl\": \"https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500\",\n    \"categoria\": {\n      \"id\": 1,\n      \"nombre\": \"Videojuegos\"\n    }\n  },\n  {\n    \"id\": 2,\n    \"codigo\": \"PROD-MC\",\n    \"nombre\": \"Minecraft\",\n    \"descripcion\": \"Construye y explora tu propio mundo con bloques infinitos.\",\n    \"precio\": 2695.0,\n    \"stock\": 4,\n    \"stockCritico\": 5,\n    \"esOferta\": true,\n    \"precioOferta\": 1990.0,\n    \"imagenUrl\": \"https://images.unsplash.com/photo-1627856013091-fed6e4e30025?w=500\",\n    \"categoria\": {\n      \"id\": 1,\n      \"nombre\": \"Videojuegos\"\n    }\n  }\n]")))
    })
    public ResponseEntity<List<Producto>> listar(
            @Parameter(description = "Texto para buscar productos por nombre", example = "Minecraft")
            @RequestParam(required = false) String buscar,
            @Parameter(description = "ID numérico de categoría para filtrar productos", example = "1")
            @RequestParam(required = false) Long categoria) {
        return ResponseEntity.ok(productoService.obtenerTodos(buscar, categoria));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto (CREATE)", description = "Crea un nuevo producto en el catálogo definiendo código, precios, stock, umbral crítico y categoría.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(name = "Producto Creado", value = "{\n  \"id\": 9,\n  \"codigo\": \"SNES-001\",\n  \"nombre\": \"Super Nintendo Classic Edition\",\n  \"descripcion\": \"Consola retro original restaurada con 21 juegos clásicos preinstalados.\",\n  \"precio\": 79990.0,\n  \"stock\": 12,\n  \"stockCritico\": 3,\n  \"esOferta\": true,\n  \"precioOferta\": 69990.0,\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1551103782-8ab07afd45c1?w=600\",\n  \"img2\": null,\n  \"img3\": null,\n  \"categoria\": {\n    \"id\": 4,\n    \"nombre\": \"Retro\"\n  }\n}"))),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    public ResponseEntity<Producto> crear(
            @RequestBody(description = "Datos del producto a registrar (listo para Try it out)", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(name = "Ejemplo Crear Producto", value = "{\n  \"codigo\": \"SNES-001\",\n  \"nombre\": \"Super Nintendo Classic Edition\",\n  \"descripcion\": \"Consola retro original restaurada con 21 juegos clásicos preinstalados.\",\n  \"precio\": 79990.0,\n  \"stock\": 12,\n  \"stockCritico\": 3,\n  \"esOferta\": true,\n  \"precioOferta\": 69990.0,\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1551103782-8ab07afd45c1?w=600\",\n  \"categoria\": {\n    \"id\": 4\n  }\n}")))
            @org.springframework.web.bind.annotation.RequestBody Producto producto) {
        Producto nuevo = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de producto para vista admin", description = "Retorna la información completa de un producto por su ID único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(name = "Detalle Producto", value = "{\n  \"id\": 1,\n  \"codigo\": \"PROD-W3\",\n  \"nombre\": \"The Witcher 3: Wild Hunt\",\n  \"descripcion\": \"Juego de rol y acción en mundo abierto de fantasía oscura.\",\n  \"precio\": 3999.0,\n  \"stock\": 15,\n  \"stockCritico\": 3,\n  \"esOferta\": false,\n  \"precioOferta\": null,\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500\",\n  \"categoria\": {\n    \"id\": 1,\n    \"nombre\": \"Videojuegos\"\n  }\n}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID numérico del producto", example = "1", required = true)
            @PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente (UPDATE)", description = "Actualiza todos los campos de un producto: nombre, descripción, precios, existencias de inventario e imágenes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(name = "Producto Actualizado", value = "{\n  \"id\": 1,\n  \"codigo\": \"W3-GOTY\",\n  \"nombre\": \"The Witcher 3: Wild Hunt - Game of the Year\",\n  \"descripcion\": \"Edición definitiva con todos los DLCs y expansiones incluidas.\",\n  \"precio\": 4990.0,\n  \"stock\": 20,\n  \"stockCritico\": 5,\n  \"esOferta\": true,\n  \"precioOferta\": 3990.0,\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500\",\n  \"categoria\": {\n    \"id\": 1\n  }\n}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado para actualizar")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del producto a editar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Datos modificados del producto", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(name = "Ejemplo Actualizar Producto", value = "{\n  \"codigo\": \"W3-GOTY\",\n  \"nombre\": \"The Witcher 3: Wild Hunt - Game of the Year\",\n  \"descripcion\": \"Edición definitiva con todos los DLCs y expansiones incluidas.\",\n  \"precio\": 4990.0,\n  \"stock\": 20,\n  \"stockCritico\": 5,\n  \"esOferta\": true,\n  \"precioOferta\": 3990.0,\n  \"imagenUrl\": \"https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500\",\n  \"categoria\": {\n    \"id\": 1\n  }\n}")))
            @org.springframework.web.bind.annotation.RequestBody Producto producto) {
        Producto actualizado = productoService.actualizar(id, producto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (DELETE)", description = "Elimina permanentemente un producto del catálogo por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Producto eliminado con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del producto a eliminar", example = "8", required = true)
            @PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Producto eliminado con éxito"));
    }

    @GetMapping("/criticos")
    @Operation(summary = "Listar productos con stock crítico (stock <= stock_critico)", description = "Retorna todos los productos cuyo inventario actual está en o por debajo del umbral crítico configurado, requiriendo reposición inmediata.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de productos con stock crítico",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                            examples = @ExampleObject(name = "Productos Criticos", value = "[\n  {\n    \"id\": 2,\n    \"codigo\": null,\n    \"nombre\": \"Minecraft\",\n    \"precio\": 2695.0,\n    \"stock\": 4,\n    \"stockCritico\": 5,\n    \"esOferta\": true,\n    \"precioOferta\": 1990.0\n  },\n  {\n    \"id\": 4,\n    \"codigo\": null,\n    \"nombre\": \"Fortnite Skin Pack\",\n    \"precio\": 0.0,\n    \"stock\": 1,\n    \"stockCritico\": 5,\n    \"esOferta\": false,\n    \"precioOferta\": null\n  },\n  {\n    \"id\": 6,\n    \"codigo\": null,\n    \"nombre\": \"Among Us Collector\",\n    \"precio\": 499.0,\n    \"stock\": 2,\n    \"stockCritico\": 4,\n    \"esOferta\": false,\n    \"precioOferta\": null\n  },\n  {\n    \"id\": 8,\n    \"codigo\": null,\n    \"nombre\": \"Ghost of Tsushima\",\n    \"precio\": 3999.0,\n    \"stock\": 1,\n    \"stockCritico\": 5,\n    \"esOferta\": false,\n    \"precioOferta\": null\n  }\n]")))
    })
    public ResponseEntity<List<Producto>> listarCriticos() {
        return ResponseEntity.ok(productoService.obtenerCriticos());
    }
}
