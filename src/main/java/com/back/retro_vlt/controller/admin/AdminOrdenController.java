package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ordenes")
@Tag(name = "Admin - Órdenes & Boletas", description = "Gestión de boletas y compras realizadas")
public class AdminOrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes / boletas de compra (opcionalmente buscar por número)")
    public ResponseEntity<?> listar(@RequestParam(required = false) String numero) {
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
    @Operation(summary = "Eliminar boleta por parámetro de número (query param ?numero=...)")
    public ResponseEntity<?> eliminarPorParametro(@RequestParam String numero) {
        boolean ok = ordenService.eliminarPorNumero(numero);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de boleta por ID")
    public ResponseEntity<Orden> obtenerPorId(@PathVariable Long id) {
        Orden o = ordenService.obtenerPorId(id);
        if (o == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(o);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Mostrar detalle de boleta por número de orden")
    public ResponseEntity<Orden> obtenerPorNumero(@PathVariable String numero) {
        Orden o = ordenService.obtenerPorNumeroOrden(numero);
        if (o == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(o);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una boleta / orden")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Orden orden) {
        Orden actualizada = ordenService.actualizar(id, orden);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar boleta / orden por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = ordenService.eliminar(id);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }

    @DeleteMapping("/numero/{numero}")
    @Operation(summary = "Eliminar boleta / orden por su número de orden")
    public ResponseEntity<?> eliminarPorNumero(@PathVariable String numero) {
        boolean ok = ordenService.eliminarPorNumero(numero);
        if (!ok) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Boleta eliminada con éxito"));
    }
}
