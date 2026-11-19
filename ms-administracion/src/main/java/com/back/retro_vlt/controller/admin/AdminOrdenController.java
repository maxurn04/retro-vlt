package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.service.OrdenService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ordenes")
@Tag(name = "Admin - Órdenes & Boletas", description = "Gestión integral de boletas, compras y estados de despacho para administradores")
public class AdminOrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes / boletas de compra",
            description = "Retorna el listado completo de órdenes y boletas emitidas ordenadas por fecha reciente. Si se pasa el query param 'numero', retorna directamente la orden que coincida con ese código.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de órdenes o detalle de boleta",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Orden.class)),
                            examples = @ExampleObject(name = "Lista de Ordenes", value = "[\n  {\n    \"id\": 1,\n    \"numeroOrden\": \"#20240705\",\n    \"fecha\": \"2026-11-05T14:30:00\",\n    \"total\": 28775.0,\n    \"estado\": \"PAGADO\",\n    \"metodoPago\": \"Tarjeta Débito Webpay\",\n    \"calle\": \"Los Crisantemos, Edificio Norte\",\n    \"departamento\": \"Depto 603\",\n    \"region\": \"Región Metropolitana de Santiago\",\n    \"comuna\": \"Cerrillos\",\n    \"indicaciones\": \"El martes no estaremos en el depto, pero puede dejárselo con el conserje.\"\n  }\n]"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada cuando se filtró por número")
    })
    public ResponseEntity<?> listar(
            @Parameter(description = "Número o código de boleta para buscar (ej: #20240705)", example = "#20240705")
            @RequestParam(required = false) String numero) {
        if (numero != null && !numero.isBlank()) {
            Orden o = ordenService.obtenerPorNumeroOrden(numero);
            if (o == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(o);
        }
        return ResponseEntity.ok(ordenService.obtenerTodas());
    }

    @DeleteMapping
    @Operation(summary = "Eliminar boleta por parámetro de número",
            description = "Elimina permanentemente una boleta buscando por el query parameter '?numero=...'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta eliminada con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Boleta eliminada con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public ResponseEntity<?> eliminarPorParametro(
            @Parameter(description = "Número o código de la orden a eliminar", example = "#20240705", required = true)
            @RequestParam String numero) {
        boolean ok = ordenService.eliminarPorNumero(numero);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de boleta por ID", description = "Retorna el detalle completo de una orden por su identificador primario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orden.class),
                            examples = @ExampleObject(name = "Detalle Orden", value = "{\n  \"id\": 1,\n  \"numeroOrden\": \"#20240705\",\n  \"fecha\": \"2026-11-05T14:30:00\",\n  \"total\": 28775.0,\n  \"estado\": \"PAGADO\",\n  \"metodoPago\": \"Tarjeta Débito Webpay\",\n  \"calle\": \"Los Crisantemos, Edificio Norte\",\n  \"departamento\": \"Depto 603\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"indicaciones\": \"El martes no estaremos en el depto, pero puede dejárselo con el conserje.\",\n  \"cliente\": {\n    \"nombre\": \"Pedro\",\n    \"apellidos\": \"Hacker\",\n    \"correo\": \"pedro.hacker20@example.com\",\n    \"telefono\": \"+56999887766\",\n    \"run\": \"19.876.543-2\"\n  }\n}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<Orden> obtenerPorId(
            @Parameter(description = "ID numérico de la orden", example = "1", required = true)
            @PathVariable Long id) {
        Orden o = ordenService.obtenerPorId(id);
        if (o == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(o);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Mostrar detalle de boleta por número de orden", description = "Retorna el detalle de una boleta de compra dado su número de orden (con o sin prefijo #).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orden.class),
                            examples = @ExampleObject(name = "Boleta Encontrada", value = "{\n  \"id\": 1,\n  \"numeroOrden\": \"#20240705\",\n  \"fecha\": \"2026-11-05T14:30:00\",\n  \"total\": 28775.0,\n  \"estado\": \"PAGADO\",\n  \"metodoPago\": \"Tarjeta Débito Webpay\",\n  \"calle\": \"Los Crisantemos, Edificio Norte\",\n  \"departamento\": \"Depto 603\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"indicaciones\": \"El martes no estaremos en el depto, pero puede dejárselo con el conserje.\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<Orden> obtenerPorNumero(
            @Parameter(description = "Número o código alfanumérico de la orden", example = "#20240705", required = true)
            @PathVariable String numero) {
        Orden o = ordenService.obtenerPorNumeroOrden(numero);
        if (o == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(o);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una boleta / orden",
            description = "Permite a los administradores modificar el estado operativo de una orden (ej. PENDIENTE_PAGO, PAGADO, EN_PREPARACION, DESPACHADO, ENTREGADO, CANCELADO), dirección y notas de logística.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orden.class),
                            examples = @ExampleObject(name = "Orden Despachada", value = "{\n  \"id\": 1,\n  \"numeroOrden\": \"#20240705\",\n  \"fecha\": \"2026-11-05T14:30:00\",\n  \"total\": 28775.0,\n  \"estado\": \"DESPACHADO\",\n  \"metodoPago\": \"Tarjeta Débito Webpay\",\n  \"calle\": \"Los Crisantemos, Edificio Norte\",\n  \"departamento\": \"Depto 603\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"indicaciones\": \"Despachado vía Starken Seguimiento #STK-994821\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada para actualizar")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la orden a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Datos de actualización de la orden (listo para Try it out con estado DESPACHADO)", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Orden.class),
                            examples = @ExampleObject(name = "Actualizar estado a DESPACHADO", value = "{\n  \"estado\": \"DESPACHADO\",\n  \"metodoPago\": \"Tarjeta Débito Webpay\",\n  \"total\": 28775.0,\n  \"calle\": \"Los Crisantemos, Edificio Norte\",\n  \"departamento\": \"Depto 603\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"indicaciones\": \"Despachado vía Starken Seguimiento #STK-994821\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Orden orden) {
        Orden actualizada = ordenService.actualizar(id, orden);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar boleta / orden por ID", description = "Elimina permanentemente una orden y sus detalles asociados según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta eliminada con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Boleta eliminada con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la orden a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        boolean ok = ordenService.eliminar(id);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }

    @DeleteMapping("/numero/{numero}")
    @Operation(summary = "Eliminar boleta / orden por su número de orden", description = "Elimina una orden del sistema buscando por su número de orden.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta eliminada con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Boleta eliminada con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public ResponseEntity<?> eliminarPorNumero(
            @Parameter(description = "Número o código alfanumérico de la orden a eliminar", example = "#20240705", required = true)
            @PathVariable String numero) {
        boolean ok = ordenService.eliminarPorNumero(numero);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }
}
