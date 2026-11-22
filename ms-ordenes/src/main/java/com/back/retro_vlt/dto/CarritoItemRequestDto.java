package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "Payload para agregar, sincronizar o actualizar un ítem en el carrito",
    example = "{\n" +
              "  \"productoId\": 2,\n" +
              "  \"cantidad\": 1\n" +
              "}"
)
public class CarritoItemRequestDto {

    @Schema(description = "Identificador del producto en el catálogo", example = "2")
    private Long productoId;

    @Schema(description = "Alias opcional de productoId", example = "2")
    private Long id;

    @Schema(description = "Cantidad de unidades (por defecto 1)", example = "1")
    private Integer cantidad;

    @Schema(description = "Si es true, sobrescribe la cantidad con el valor recibido en lugar de sumar a la cantidad existente", example = "false")
    private Boolean sobrescribir;

    @Schema(description = "Modificador relativo (+1 o -1)", example = "1")
    private Integer modificador;

    public Long getProductoId() {
        if (productoId != null) {
            return productoId;
        }
        return id;
    }
}
