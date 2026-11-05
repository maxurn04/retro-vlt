package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ordenes")
@Tag(name = "Admin - Órdenes & Boletas", description = "Gestión de boletas y compras realizadas")
public class AdminOrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes / boletas de compra")
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenService.obtenerTodas());
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
}
