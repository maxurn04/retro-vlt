package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.dto.MetricasDashboardDto;
import com.back.retro_vlt.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@Tag(name = "Admin - Dashboard", description = "Métricas y KPIs del panel administrativo")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metricas")
    @Operation(summary = "Obtener KPIs del Dashboard (ventas, stock, usuarios, alertas)")
    public ResponseEntity<MetricasDashboardDto> obtenerMetricas() {
        return ResponseEntity.ok(dashboardService.obtenerMetricas());
    }
}
