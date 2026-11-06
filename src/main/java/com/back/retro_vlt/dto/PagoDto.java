package com.back.retro_vlt.dto;

import lombok.Data;

@Data
public class PagoDto {
    private String metodoPago = "TARJETA_DEBITO";
    private Boolean simularError = false;
}
