package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.dto.MetricasDashboardDto;
import com.back.retro_vlt.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminDashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private AdminDashboardController controller;

    @Test
    void testObtenerMetricas() {
        MetricasDashboardDto metricas = new MetricasDashboardDto(10.0, 50000.0, 25.0, 100.0, 50.0, 2.0);
        when(dashboardService.obtenerMetricas()).thenReturn(metricas);

        ResponseEntity<MetricasDashboardDto> response = controller.obtenerMetricas();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(10.0, response.getBody().getTotalCompras());
        assertEquals(50000.0, response.getBody().getIngresosTotales());
    }
}
