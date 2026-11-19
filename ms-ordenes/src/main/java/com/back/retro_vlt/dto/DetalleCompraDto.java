package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
    description = "Detalle del producto o ítem en la orden de compra",
    example = "{\n" +
              "  \"productoId\": 2,\n" +
              "  \"cantidad\": 2,\n" +
              "  \"precio\": 1990.0\n" +
              "}"
)
public class DetalleCompraDto extends CompraDto.ItemCompraDto {
}
