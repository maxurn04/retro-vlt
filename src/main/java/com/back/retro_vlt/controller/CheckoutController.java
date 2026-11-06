package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.CheckoutRequestDto;
import com.back.retro_vlt.dto.PagarRequestDto;
import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Tienda - Carrito & Checkout", description = "Endpoints para el flujo de compra, despacho y simulación de pago")
public class CheckoutController {

    @Autowired
    private OrdenService ordenService;

    @PostMapping("/checkout")
    @Operation(summary = "Crear orden de compra (checkout) a partir del carrito y datos de entrega")
    public ResponseEntity<?> crearCheckout(@RequestBody CheckoutRequestDto dto) {
        Orden orden = ordenService.crearCheckout(dto);
        if (orden == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar la orden. Verifique los productos y datos ingresados."));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("ordenId", orden.getId());
        resp.put("numeroOrden", orden.getNumeroOrden());
        resp.put("total", orden.getTotal());
        resp.put("estado", orden.getEstado());
        resp.put("metodoPago", orden.getMetodoPago());
        resp.put("mensaje", "Orden registrada con éxito. Lista para procesar pago.");
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/checkout/{ordenId}/pagar")
    @Operation(summary = "Procesar y simular pago de la orden (éxito o rechazo)")
    public ResponseEntity<?> procesarPago(@PathVariable Long ordenId, @RequestBody(required = false) PagarRequestDto dto) {
        if (dto == null) {
            dto = new PagarRequestDto();
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
            err.put("motivo", "Fondos insuficientes o transacción rechazada por el banco.");
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
        exito.put("mensaje", "Pago completado con éxito. Se ha generado su boleta.");
        return ResponseEntity.ok(exito);
    }

    @GetMapping("/ordenes/{numeroOrden}")
    @Operation(summary = "Obtener el resumen de la boleta por su número de orden (#20240705)")
    public ResponseEntity<?> obtenerBoleta(@PathVariable String numeroOrden) {
        Orden orden = ordenService.obtenerPorNumeroOrden(numeroOrden);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orden);
    }
}
