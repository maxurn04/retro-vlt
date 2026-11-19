package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(
    description = "Objeto de transferencia para procesar una compra a partir del carrito de la tienda",
    example = "{\n" +
              "  \"cliente\": {\n" +
              "    \"usuarioId\": 2,\n" +
              "    \"nombre\": \"Pedro\",\n" +
              "    \"apellidos\": \"Hacker\",\n" +
              "    \"correo\": \"pedro.hacker20@example.com\"\n" +
              "  },\n" +
              "  \"direccion\": {\n" +
              "    \"calle\": \"Los Crisantemos, Edificio Norte\",\n" +
              "    \"departamento\": \"Depto 603\",\n" +
              "    \"region\": \"Región Metropolitana de Santiago\",\n" +
              "    \"comuna\": \"Cerrillos\",\n" +
              "    \"indicaciones\": \"Dejar con conserje si no responde\"\n" +
              "  },\n" +
              "  \"metodoPago\": \"Tarjeta Visa / Mastercard\",\n" +
              "  \"items\": [\n" +
              "    {\n" +
              "      \"productoId\": 2,\n" +
              "      \"cantidad\": 2,\n" +
              "      \"precio\": 1990.0\n" +
              "    },\n" +
              "    {\n" +
              "      \"productoId\": 1,\n" +
              "      \"cantidad\": 1,\n" +
              "      \"precio\": 3999.0\n" +
              "    }\n" +
              "  ]\n" +
              "}"
)
public class CompraDto {

    @Schema(description = "Información del cliente que realiza la compra")
    private ClienteDto cliente;

    @Schema(description = "Dirección de despacho físico de la compra")
    private DireccionDto direccion;

    @Schema(description = "Lista de ítems o productos comprados")
    private List<ItemCompraDto> items;

    @Schema(description = "Alias opcional de items para compatibilidad con el frontend")
    private List<ItemCompraDto> productos;

    @Schema(description = "Método de pago seleccionado para la orden", example = "Tarjeta Visa / Mastercard")
    private String metodoPago;

    @Schema(description = "Total calculado en la vista previa del carrito", example = "7979.0")
    private Double total;

    public List<ItemCompraDto> getItems() {
        if (items != null && !items.isEmpty()) {
            return items;
        }
        return productos;
    }

    @Data
    @Schema(description = "Datos del cliente y contacto para el pedido")
    public static class ClienteDto {
        @Schema(description = "Nombre de pila del cliente", example = "Pedro")
        private String nombre;

        @Schema(description = "Apellidos del cliente", example = "Hacker")
        private String apellidos;

        @Schema(description = "Correo electrónico del cliente", example = "pedro.hacker20@example.com")
        private String correo;

        @Schema(description = "ID del usuario en caso de estar registrado en el sistema", example = "2")
        private Long usuarioId;

        @Schema(description = "Calle y número del domicilio", example = "Los Crisantemos, Edificio Norte")
        private String calle;

        @Schema(description = "Número de departamento, oficina o torre", example = "Depto 603")
        private String departamento;

        @Schema(description = "Región de Chile para el despacho", example = "Región Metropolitana de Santiago")
        private String region;

        @Schema(description = "Comuna de entrega", example = "Cerrillos")
        private String comuna;

        @Schema(description = "Indicaciones para el repartidor", example = "Dejar con conserje si no responde")
        private String indicaciones;
    }

    @Data
    @Schema(description = "Dirección de despacho del pedido")
    public static class DireccionDto {
        @Schema(description = "Calle y número del domicilio", example = "Los Crisantemos, Edificio Norte")
        private String calle;

        @Schema(description = "Número de departamento u oficina", example = "Depto 603")
        private String departamento;

        @Schema(description = "Región de entrega", example = "Región Metropolitana de Santiago")
        private String region;

        @Schema(description = "Comuna de entrega", example = "Cerrillos")
        private String comuna;

        @Schema(description = "Instrucciones especiales para el repartidor", example = "Dejar en conserjería si no responden")
        private String indicaciones;
    }

    @Data
    @Schema(description = "Detalle del producto o ítem comprado")
    public static class ItemCompraDto {
        @Schema(description = "ID del producto existente en el catálogo", example = "2")
        private Long productoId;

        @Schema(description = "Alias opcional de productoId", example = "2")
        private Long id;

        @Schema(description = "Cantidad de unidades del producto", example = "2")
        private Integer cantidad;

        @Schema(description = "Precio unitario del producto al momento de comprar", example = "1990.0")
        private Double precio;

        public Long getProductoId() {
            if (productoId != null) {
                return productoId;
            }
            return id;
        }
    }
}
