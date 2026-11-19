package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    description = "Payload para procesar o simular el pago de una orden de compra",
    example = "{\n" +
              "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
              "  \"simularError\": false\n" +
              "}"
)
public class PagoDto {

    @Schema(
        description = "Método o pasarela de pago seleccionada",
        example = "Tarjeta Visa / Mastercard",
        defaultValue = "TARJETA_DEBITO"
    )
    private String metodoPago = "TARJETA_DEBITO";

    @Schema(
        description = "Bandera para simular error bancario o fondos insuficientes (false = exitoso, true = rechazado)",
        example = "false",
        defaultValue = "false"
    )
    private Boolean simularError = false;
}
