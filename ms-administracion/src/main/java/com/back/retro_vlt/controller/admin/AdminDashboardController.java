package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.dto.MetricasDashboardDto;
import com.back.retro_vlt.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@Tag(name = "Admin - Dashboard", description = "Métricas y KPIs del panel administrativo en tiempo real")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metricas")
    @Operation(summary = "Obtener KPIs del Dashboard (ventas, stock, usuarios, alertas)",
            description = "Calcula y retorna los indicadores clave de rendimiento (KPIs): compras totales, ingresos totales en CLP, productos en catálogo, unidades físicas en stock, usuarios registrados y alertas por productos con stock crítico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas consolidadas obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MetricasDashboardDto.class),
                            examples = @ExampleObject(name = "Ejemplo Metricas Reales", value = "{\n  \"totalCompras\": 1.0,\n  \"ingresosTotales\": 28775.0,\n  \"productosInventario\": 8.0,\n  \"stockTotalUnidades\": 47.0,\n  \"usuariosRegistrados\": 2.0,\n  \"productosCriticosAlerta\": 4.0\n}")))
    })
    public ResponseEntity<MetricasDashboardDto> obtenerMetricas() {
        return ResponseEntity.ok(dashboardService.obtenerMetricas());
    }
}
