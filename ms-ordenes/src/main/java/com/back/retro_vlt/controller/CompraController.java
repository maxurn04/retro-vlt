package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.CompraDto;
import com.back.retro_vlt.dto.PagoDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Tienda - Carrito & Compra", description = "Endpoints para el flujo completo de compra, checkout, despacho y simulación de pago")
public class CompraController {

    @Autowired
    private OrdenService ordenService;

    @PostMapping({"/compra", "/checkout"})
    @Operation(
        summary = "Crear orden de compra a partir del carrito y datos de entrega",
        description = "Registra una nueva orden de compra con los productos del carrito, datos de cliente y dirección de despacho. La orden queda creada en estado 'PENDIENTE_PAGO' con un número de orden generado (#yyyyMMdd-XXXX) lista para procesar el pago."
    )
    @RequestBody(
        description = "Datos completos de la compra (cliente, dirección, ítems del carrito y método de pago)",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CompraDto.class),
            examples = {
                @ExampleObject(
                    name = "CompraClienteRegistrado",
                    summary = "Compra de cliente registrado (Minecraft y The Witcher 3)",
                    description = "Payload con cliente registrado (usuarioId 2), dirección de entrega en Cerrillos y dos productos existentes en stock",
                    value = "{\n" +
                            "  \"cliente\": {\n" +
                            "    \"usuarioId\": 2,\n" +
                            "    \"nombre\": \"Pedro\",\n" +
                            "    \"apellidos\": \"Hacker\",\n" +
                            "    \"correo\": \"pedro.hacker20@example.com\"\n" +
                            "  },\n" +
                            "  \"direccion\": {\n" +
                            "    \"calle\": \"Los Crisantemos, Edificio Norte\",\n" +
                            "    \"departamento\": \"Depto 603\",\n" +
                            "    \"region\": \"Región Metropolitana de Santiago\",\n" +
                            "    \"comuna\": \"Cerrillos\",\n" +
                            "    \"indicaciones\": \"Dejar con conserje si no responde\"\n" +
                            "  },\n" +
                            "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
                            "  \"items\": [\n" +
                            "    {\n" +
                            "      \"productoId\": 2,\n" +
                            "      \"cantidad\": 2,\n" +
                            "      \"precio\": 1990.0\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"productoId\": 1,\n" +
                            "      \"cantidad\": 1,\n" +
                            "      \"precio\": 3999.0\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                ),
                @ExampleObject(
                    name = "CompraInvitado",
                    summary = "Compra de cliente invitado (Red Dead Redemption 2)",
                    description = "Compra directa sin usuarioId previo, especificando datos de contacto y entrega",
                    value = "{\n" +
                            "  \"cliente\": {\n" +
                            "    \"nombre\": \"Constanza\",\n" +
                            "    \"apellidos\": \"Valenzuela\",\n" +
                            "    \"correo\": \"c.valenzuela@example.com\",\n" +
                            "    \"calle\": \"Av. Providencia 1234\",\n" +
                            "    \"departamento\": \"Oficina 402\",\n" +
                            "    \"region\": \"Región Metropolitana de Santiago\",\n" +
                            "    \"comuna\": \"Providencia\",\n" +
                            "    \"indicaciones\": \"Horario de oficina 9 a 18 hrs\"\n" +
                            "  },\n" +
                            "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
                            "  \"items\": [\n" +
                            "    {\n" +
                            "      \"productoId\": 3,\n" +
                            "      \"cantidad\": 1,\n" +
                            "      \"precio\": 5999.0\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                )
            }
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Orden de compra creada exitosamente y lista para procesar el pago",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "OrdenRegistradaExitosa",
                    summary = "Ejemplo de respuesta al registrar orden",
                    value = "{\n" +
                            "  \"ordenId\": 2,\n" +
                            "  \"numeroOrden\": \"#20261119-4821\",\n" +
                            "  \"total\": 7979.0,\n" +
                            "  \"estado\": \"PENDIENTE_PAGO\",\n" +
                            "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
                            "  \"mensaje\": \"Orden registrada con exito. Lista para procesar pago.\"\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de compra inválidos o productos inexistentes",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "ErrorCrearOrden",
                    summary = "Error de validación",
                    value = "{\n  \"error\": \"No se pudo registrar la orden. Verifique los productos y datos ingresados.\"\n}"
                )
            )
        )
    })
    public ResponseEntity<?> procesarCompra(@org.springframework.web.bind.annotation.RequestBody CompraDto dto) {
        Orden orden = ordenService.procesarCompra(dto);
        if (orden == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar la orden. Verifique los productos y datos ingresados."));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("ordenId", orden.getId());
        resp.put("numeroOrden", orden.getNumeroOrden());
        resp.put("total", orden.getTotal());
        resp.put("estado", orden.getEstado());
        resp.put("metodoPago", orden.getMetodoPago());
        resp.put("mensaje", "Orden registrada con exito. Lista para procesar pago.");
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping({"/compra/{ordenId}/pagar", "/checkout/{ordenId}/pagar"})
    @Operation(
        summary = "Procesar y simular pago de la orden (éxito o rechazo)",
        description = "Simula la transacción de pago para una orden registrada. Cuando 'simularError' es false, descuenta automáticamente el stock de los productos comprados y cambia el estado a 'PAGADO'. Si 'simularError' es true, cambia el estado a 'FALLIDO' simulando un rechazo de fondos o tarjeta."
    )
    @RequestBody(
        description = "Opciones para procesar o simular el pago",
        required = false,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PagoDto.class),
            examples = {
                @ExampleObject(
                    name = "PagoExitoso",
                    summary = "Pago Aprobado (Tarjeta Visa / Mastercard)",
                    description = "Simula un pago exitoso que liquida la orden y actualiza el stock",
                    value = "{\n  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n  \"simularError\": false\n}"
                ),
                @ExampleObject(
                    name = "PagoRechazado",
                    summary = "Simular Pago Rechazado / Fondos Insuficientes",
                    description = "Simula una transacción rechazada por el banco emisor",
                    value = "{\n  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n  \"simularError\": true\n}"
                )
            }
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pago completado con éxito, stock actualizado y boleta generada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PagoAprobadoRespuesta",
                    summary = "Respuesta de pago exitoso",
                    value = "{\n" +
                            "  \"exito\": true,\n" +
                            "  \"numeroOrden\": \"#20240705\",\n" +
                            "  \"estado\": \"PAGADO\",\n" +
                            "  \"totalPagado\": 28775.0,\n" +
                            "  \"fecha\": \"2026-11-05T14:30:00\",\n" +
                            "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
                            "  \"mensaje\": \"Pago completado con exito. Se ha generado su boleta.\"\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Transacción rechazada por el banco emisor o fondos insuficientes",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PagoRechazadoRespuesta",
                    summary = "Respuesta de pago fallido",
                    value = "{\n" +
                            "  \"exito\": false,\n" +
                            "  \"numeroOrden\": \"#20240705\",\n" +
                            "  \"estado\": \"FALLIDO\",\n" +
                            "  \"motivo\": \"Fondos insuficientes o transaccion rechazada por el banco.\",\n" +
                            "  \"accion\": \"VOLVER A REALIZAR EL PAGO\"\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Orden no encontrada para el ID especificado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "OrdenNoEncontrada",
                    summary = "Orden inexistente",
                    value = "{\n  \"error\": \"Orden no encontrada\"\n}"
                )
            )
        )
    })
    public ResponseEntity<?> procesarPago(
            @Parameter(description = "ID numérico de la orden a pagar (ej. 1)", example = "1", required = true)
            @PathVariable Long ordenId,
            @org.springframework.web.bind.annotation.RequestBody(required = false) PagoDto dto) {
        if (dto == null) {
            dto = new PagoDto();
        }
        Orden orden = ordenService.procesarPago(ordenId, dto);
        if (orden == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Orden no encontrada"));
        }

        if ("FALLIDO".equalsIgnoreCase(orden.getEstado())) {
            Map<String, Object> err = new HashMap<>();
            err.put("exito", false);
            err.put("numeroOrden", orden.getNumeroOrden());
            err.put("estado", orden.getEstado());
            err.put("motivo", "Fondos insuficientes o transaccion rechazada por el banco.");
            err.put("accion", "VOLVER A REALIZAR EL PAGO");
            return ResponseEntity.badRequest().body(err);
        }

        Map<String, Object> exito = new HashMap<>();
        exito.put("exito", true);
        exito.put("numeroOrden", orden.getNumeroOrden());
        exito.put("estado", orden.getEstado());
        exito.put("totalPagado", orden.getTotal());
        exito.put("fecha", orden.getFecha());
        exito.put("metodoPago", orden.getMetodoPago());
        exito.put("mensaje", "Pago completado con exito. Se ha generado su boleta.");
        return ResponseEntity.ok(exito);
    }

    @GetMapping({"/ordenes", "/boletas"})
    @Operation(
        summary = "Listar todas las boletas y órdenes de compra",
        description = "Retorna el listado completo de boletas/órdenes generadas en la tienda para gestión y panel de administración, ordenadas cronológicamente por fecha descendente."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de todas las boletas obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Orden.class))
            )
        )
    })
    public ResponseEntity<List<Orden>> listarTodasBoletas() {
        return ResponseEntity.ok(ordenService.obtenerTodas());
    }

    @GetMapping({"/ordenes/usuario/{usuarioId}", "/boletas/usuario/{usuarioId}"})
    @Operation(
        summary = "Listar boletas y órdenes de un usuario específico",
        description = "Retorna el listado histórico de boletas pertenecientes al usuario especificado por su identificador único (usuarioId)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de boletas del usuario obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Orden.class))
            )
        )
    })
    public ResponseEntity<List<Orden>> listarBoletasPorUsuario(
            @Parameter(description = "ID único del usuario", example = "2", required = true)
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping({"/ordenes/{numeroOrden}", "/boletas/{numeroOrden}"})
    @Operation(
        summary = "Obtener el resumen de la boleta por su número de orden (#20240705)",
        description = "Consulta y retorna el detalle completo de la orden o boleta identificada por su número de orden (admite formato con numeral '#20240705' o sólo dígitos '20240705'). Devuelve datos de despacho, cliente, ítems comprados y total."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Detalle completo de la boleta y orden encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Orden.class),
                examples = @ExampleObject(
                    name = "BoletaEncontrada",
                    summary = "Ejemplo de boleta #20240705",
                    value = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"numeroOrden\": \"#20240705\",\n" +
                            "  \"fecha\": \"2026-11-05T14:30:00\",\n" +
                            "  \"total\": 28775.0,\n" +
                            "  \"estado\": \"PAGADO\",\n" +
                            "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
                            "  \"calle\": \"Los Crisantemos, Edificio Norte\",\n" +
                            "  \"departamento\": \"Depto 603\",\n" +
                            "  \"region\": \"Región Metropolitana de Santiago\",\n" +
                            "  \"comuna\": \"Cerrillos\",\n" +
                            "  \"indicaciones\": \"El martes no estaremos en el depto, pero puede dejárselo con el conserje.\",\n" +
                            "  \"cliente\": {\n" +
                            "    \"nombre\": \"Pedro\",\n" +
                            "    \"apellidos\": \"Hacker\",\n" +
                            "    \"correo\": \"pedro.hacker20@example.com\",\n" +
                            "    \"telefono\": \"+56999887766\",\n" +
                            "    \"run\": \"\",\n" +
                            "    \"calle\": \"Los Crisantemos, Edificio Norte\",\n" +
                            "    \"departamento\": \"Depto 603\",\n" +
                            "    \"region\": \"Región Metropolitana de Santiago\",\n" +
                            "    \"comuna\": \"Cerrillos\"\n" +
                            "  },\n" +
                            "  \"productos\": [\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"codigo\": \"BAR000002\",\n" +
                            "      \"nombre\": \"Minecraft\",\n" +
                            "      \"precio\": 2695.0,\n" +
                            "      \"cantidad\": 4,\n" +
                            "      \"subtotal\": 10780.0\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"codigo\": \"BAR000003\",\n" +
                            "      \"nombre\": \"Red Dead Redemption 2\",\n" +
                            "      \"precio\": 5999.0,\n" +
                            "      \"cantidad\": 1,\n" +
                            "      \"subtotal\": 5999.0\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontró ninguna orden o boleta con el número proporcionado"
        )
    })
    public ResponseEntity<?> obtenerBoleta(
            @Parameter(description = "Número de orden o boleta (ej. '#20240705' o '20240705')", example = "#20240705", required = true)
            @PathVariable String numeroOrden) {
        Orden orden = ordenService.obtenerPorNumeroOrden(numeroOrden);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orden);
    }
}
