package com.back.retro_vlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricasDashboardDto {
    private Double totalCompras;
    private Double ingresosTotales;
    private Double productosInventario;
    private Double stockTotalUnidades;
    private Double usuariosRegistrados;
    private Double productosCriticosAlerta;
}
