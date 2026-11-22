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
    description = "Representación de un ítem en el carrito de compras del usuario",
    example = "{\n" +
              "  \"id\": 1,\n" +
              "  \"productoId\": 2,\n" +
              "  \"nombre\": \"Minecraft\",\n" +
              "  \"precio\": 1990.0,\n" +
              "  \"cantidad\": 2,\n" +
              "  \"imagenUrl\": \"https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg\",\n" +
              "  \"img\": \"https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg\",\n" +
              "  \"subtotal\": 3980.0\n" +
              "}"
)
public class CarritoItemDto {

    @Schema(description = "Identificador único del registro de carrito", example = "1")
    private Long id;

    @Schema(description = "Identificador del producto en el catálogo", example = "2")
    private Long productoId;

    @Schema(description = "Nombre descriptivo del producto", example = "Minecraft")
    private String nombre;

    @Schema(description = "Precio unitario vigente (aplica oferta si corresponde)", example = "1990.0")
    private Double precio;

    @Schema(description = "Cantidad de unidades en el carrito", example = "2")
    private Integer cantidad;

    @Schema(description = "URL de la imagen del producto", example = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg")
    private String imagenUrl;

    @Schema(description = "Alias de imagenUrl para compatibilidad directa con el frontend", example = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7d.jpg")
    private String img;

    @Schema(description = "Subtotal calculado para el ítem (precio * cantidad)", example = "3980.0")
    private Double subtotal;
}
