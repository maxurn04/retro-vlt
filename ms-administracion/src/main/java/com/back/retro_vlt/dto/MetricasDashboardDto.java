package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Métricas consolidadas y KPIs para el panel de administración")
public class MetricasDashboardDto {

    @Schema(description = "Total histórico de órdenes y compras registradas", example = "1.0")
    private Double totalCompras;

    @Schema(description = "Ingresos acumulados totales por ventas en CLP", example = "28775.0")
    private Double ingresosTotales;

    @Schema(description = "Cantidad total de productos distintos en el catálogo", example = "8.0")
    private Double productosInventario;

    @Schema(description = "Suma total de unidades físicas disponibles en stock", example = "47.0")
    private Double stockTotalUnidades;

    @Schema(description = "Número total de usuarios registrados en el sistema", example = "2.0")
    private Double usuariosRegistrados;

    @Schema(description = "Cantidad de productos cuyo stock está en o por debajo del umbral crítico", example = "4.0")
    private Double productosCriticosAlerta;
}
