package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.service.ProductoService;
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
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Tienda - Productos", description = "Endpoints para el catálogo de la tienda pública")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(
        summary = "Listar catálogo de productos (con filtro de búsqueda o categoría)",
        description = "Permite consultar el catálogo general de productos de la tienda pública, permitiendo filtrar opcionalmente por texto de búsqueda en nombre/descripción o por identificador de categoría."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Catálogo de productos recuperado exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                examples = @ExampleObject(
                    name = "ListadoProductosEjemplo",
                    summary = "Ejemplo de catálogo con productos filtrados o generales",
                    value = "[{\"id\":1,\"codigo\":\"NES-001\",\"nombre\":\"Nintendo Entertainment System (NES)\",\"descripcion\":\"Consola clásica de 8 bits restaurada en perfecto estado con dos mandos originales.\",\"precio\":89990.0,\"stock\":15,\"stockCritico\":5,\"esOferta\":true,\"precioOferta\":74990.0,\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80\",\"img2\":\"https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&w=600&q=80\",\"img3\":\"https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=600&q=80\",\"categoria\":{\"id\":1,\"nombre\":\"Consolas\",\"descripcion\":\"Consolas retro clásicas\",\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f\"}}]"
                )
            )
        )
    })
    public ResponseEntity<List<Producto>> listar(
            @Parameter(description = "Texto de búsqueda para filtrar por nombre o descripción del producto (ej: 'Nintendo', 'Sega', 'Mario')", example = "Nintendo", required = false)
            @RequestParam(required = false) String buscar,
            @Parameter(description = "ID numérico de la categoría para filtrar productos (ej: 1 para Consolas)", example = "1", required = false)
            @RequestParam(required = false) Long categoria) {
        return ResponseEntity.ok(productoService.obtenerTodos(buscar, categoria));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener detalle de un producto por ID",
        description = "Recupera la ficha completa y detallada de un producto específico mediante su ID único, incluyendo precios, imágenes, stock disponible y categoría."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class),
                examples = @ExampleObject(
                    name = "DetalleProductoEjemplo",
                    summary = "Ejemplo de ficha de producto",
                    value = "{\"id\":1,\"codigo\":\"NES-001\",\"nombre\":\"Nintendo Entertainment System (NES)\",\"descripcion\":\"Consola clásica de 8 bits restaurada en perfecto estado con dos mandos originales.\",\"precio\":89990.0,\"stock\":15,\"stockCritico\":5,\"esOferta\":true,\"precioOferta\":74990.0,\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80\",\"img2\":\"https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&w=600&q=80\",\"img3\":\"https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=600&q=80\",\"categoria\":{\"id\":1,\"nombre\":\"Consolas\",\"descripcion\":\"Consolas retro clásicas\",\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró ningún producto con el ID especificado",
            content = @Content
        )
    })
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "Identificador único numérico del producto (ej: 1)", example = "1", required = true)
            @PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/ofertas")
    @Operation(
        summary = "Obtener todos los productos en oferta",
        description = "Retorna el listado exclusivo de productos que cuentan con descuento activo (esOferta = true) y sus respectivos precios rebajados de promoción."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de ofertas obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                examples = @ExampleObject(
                    name = "ProductosOfertaEjemplo",
                    summary = "Ejemplo de productos en oferta promocional",
                    value = "[{\"id\":1,\"codigo\":\"NES-001\",\"nombre\":\"Nintendo Entertainment System (NES)\",\"descripcion\":\"Consola clásica de 8 bits restaurada.\",\"precio\":89990.0,\"stock\":15,\"stockCritico\":5,\"esOferta\":true,\"precioOferta\":74990.0,\"imagenUrl\":\"https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80\",\"categoria\":{\"id\":1,\"nombre\":\"Consolas\"}}]"
                )
            )
        )
    })
    public ResponseEntity<List<Producto>> listarOfertas() {
        return ResponseEntity.ok(productoService.obtenerOfertas());
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(
        summary = "Listar productos de una categoría específica",
        description = "Retorna todos los productos asociados al identificador de categoría proporcionado en la ruta URL."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de productos de la categoría obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                examples = @ExampleObject(
                    name = "ProductosPorCategoriaEjemplo",
                    summary = "Ejemplo de productos filtrados por ID de categoría",
                    value = "[{\"id\":1,\"codigo\":\"NES-001\",\"nombre\":\"Nintendo Entertainment System (NES)\",\"precio\":89990.0,\"stock\":15,\"categoria\":{\"id\":1,\"nombre\":\"Consolas\"}}]"
                )
            )
        )
    })
    public ResponseEntity<List<Producto>> listarPorCategoria(
            @Parameter(description = "Identificador numérico de la categoría a consultar (ej: 1 para Consolas)", example = "1", required = true)
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.obtenerTodos(null, categoriaId));
    }

    @GetMapping("/{id}/relacionados")
    @Operation(
        summary = "Obtener productos relacionados (misma categoría)",
        description = "Busca y retorna una lista de hasta 4 productos sugeridos o recomendados que comparten la misma categoría que el producto referenciado por su ID (excluyendo el producto consultado)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de productos relacionados obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class)),
                examples = @ExampleObject(
                    name = "ProductosRelacionadosEjemplo",
                    summary = "Ejemplo de productos sugeridos de la misma categoría",
                    value = "[{\"id\":2,\"codigo\":\"SNES-002\",\"nombre\":\"Super Nintendo Entertainment System (SNES)\",\"descripcion\":\"Consola clásica de 16 bits de Nintendo.\",\"precio\":109990.0,\"stock\":8,\"stockCritico\":3,\"esOferta\":false,\"categoria\":{\"id\":1,\"nombre\":\"Consolas\"}}]"
                )
            )
        )
    })
    public ResponseEntity<List<Producto>> listarRelacionados(
            @Parameter(description = "Identificador del producto base para buscar otros productos de su misma categoría (ej: 1)", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerRelacionados(id));
    }
}
