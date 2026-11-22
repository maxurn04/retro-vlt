package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.CarritoItemDto;
import com.back.retro_vlt.dto.CarritoItemRequestDto;
import com.back.retro_vlt.service.CarritoService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Tienda - Carrito de Compras", description = "Endpoints para la persistencia, consulta y sincronización del carrito de compras por usuario")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    @Operation(
        summary = "Obtener los ítems del carrito de un usuario",
        description = "Retorna el listado completo de productos guardados en el carrito para el usuario especificado, incluyendo ID de item, ID de producto, nombre, precio activo, cantidad, imagen y subtotal."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de ítems del carrito obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CarritoItemDto.class)),
                examples = @ExampleObject(
                    name = "CarritoEjemplo",
                    summary = "Ejemplo de carrito con productos",
                    value = "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"productoId\": 2,\n" +
                            "    \"nombre\": \"Minecraft\",\n" +
                            "    \"precio\": 1990.0,\n" +
                            "    \"cantidad\": 2,\n" +
                            "    \"imagenUrl\": \"https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg\",\n" +
                            "    \"img\": \"https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg\",\n" +
                            "    \"subtotal\": 3980.0\n" +
                            "  }\n" +
                            "]"
                )
            )
        )
    })
    public ResponseEntity<List<CarritoItemDto>> obtenerCarrito(
            @Parameter(description = "ID del usuario propietario del carrito", example = "2", required = true)
            @PathVariable Long usuarioId) {
        List<CarritoItemDto> items = carritoService.obtenerItemsPorUsuario(usuarioId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{usuarioId}")
    @Operation(
        summary = "Guardar o sincronizar un ítem en el carrito del usuario",
        description = "Agrega un producto al carrito del usuario o incrementa/actualiza su cantidad si ya existía en la base de datos."
    )
    @RequestBody(
        description = "Datos del producto y cantidad a guardar o sincronizar en el carrito",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CarritoItemRequestDto.class),
            examples = @ExampleObject(
                name = "AgregarProducto",
                summary = "Agregar 2 unidades del producto ID 2 al carrito",
                value = "{\n  \"productoId\": 2,\n  \"cantidad\": 2\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ítem guardado o sincronizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoItemDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (productoId requerido)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\n  \"error\": \"productoId es obligatorio\"\n}")
            )
        )
    })
    public ResponseEntity<?> guardarItem(
            @Parameter(description = "ID del usuario propietario del carrito", example = "2", required = true)
            @PathVariable Long usuarioId,
            @org.springframework.web.bind.annotation.RequestBody(required = false) CarritoItemRequestDto request,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Integer cantidad) {

        if (request == null) {
            request = new CarritoItemRequestDto();
        }
        if (request.getProductoId() == null && productoId != null) {
            request.setProductoId(productoId);
        }
        if (request.getCantidad() == null && cantidad != null) {
            request.setCantidad(cantidad);
        }

        if (request.getProductoId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "productoId es obligatorio"));
        }

        CarritoItemDto item = carritoService.guardarOSincronizarItem(usuarioId, request);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    @PutMapping("/{usuarioId}/item/{productoId}")
    @Operation(
        summary = "Actualizar la cantidad de un producto en el carrito",
        description = "Modifica la cantidad asignada a un producto específico en el carrito del usuario. Si la cantidad resulta menor o igual a cero, el producto es removido del carrito."
    )
    @RequestBody(
        description = "Nueva cantidad o modificador de cantidad",
        required = false,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CarritoItemRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "EstablecerCantidad",
                    summary = "Establecer cantidad exacta a 3",
                    value = "{\n  \"cantidad\": 3\n}"
                ),
                @ExampleObject(
                    name = "ModificarCantidad",
                    summary = "Sumar 1 a la cantidad actual",
                    value = "{\n  \"modificador\": 1\n}"
                )
            }
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cantidad actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CarritoItemDto.class)
            )
        )
    })
    public ResponseEntity<?> actualizarCantidad(
            @Parameter(description = "ID del usuario", example = "2", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID del producto", example = "2", required = true)
            @PathVariable Long productoId,
            @org.springframework.web.bind.annotation.RequestBody(required = false) CarritoItemRequestDto request,
            @RequestParam(required = false) Integer cantidad,
            @RequestParam(required = false) Integer modificador) {

        Integer cant = cantidad;
        Integer mod = modificador;

        if (request != null) {
            if (request.getCantidad() != null) {
                cant = request.getCantidad();
            }
            if (request.getModificador() != null) {
                mod = request.getModificador();
            }
        }

        CarritoItemDto actualizado = carritoService.actualizarCantidad(usuarioId, productoId, cant, mod);
        if (actualizado == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("exito", true);
            resp.put("mensaje", "Producto eliminado o no existente en el carrito");
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{usuarioId}/item/{productoId}")
    @Operation(
        summary = "Eliminar un producto específico del carrito del usuario",
        description = "Remueve permanentemente el ítem del carrito identificado por usuarioId y productoId."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto eliminado exitosamente del carrito",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\n  \"exito\": true,\n  \"mensaje\": \"Producto eliminado del carrito\"\n}")
            )
        )
    })
    public ResponseEntity<?> eliminarItem(
            @Parameter(description = "ID del usuario", example = "2", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID del producto a eliminar", example = "2", required = true)
            @PathVariable Long productoId) {
        boolean eliminado = carritoService.eliminarItem(usuarioId, productoId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("exito", eliminado);
        resp.put("mensaje", eliminado ? "Producto eliminado del carrito" : "Producto no encontrado en el carrito");
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{usuarioId}")
    @Operation(
        summary = "Vaciar el carrito completo de un usuario",
        description = "Elimina todos los productos guardados en el carrito para el usuario especificado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrito vaciado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\n  \"exito\": true,\n  \"mensaje\": \"Carrito vaciado exitosamente\"\n}")
            )
        )
    })
    public ResponseEntity<?> vaciarCarrito(
            @Parameter(description = "ID del usuario cuyo carrito se vaciará", example = "2", required = true)
            @PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("exito", true);
        resp.put("mensaje", "Carrito vaciado exitosamente");
        return ResponseEntity.ok(resp);
    }
}
