package com.back.retro_vlt.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraDto {

    private ClienteDto cliente;
    private DireccionDto direccion;
    private List<ItemCompraDto> items;
    private List<ItemCompraDto> productos;
    private String metodoPago;
    private Double total;

    public List<ItemCompraDto> getItems() {
        if (items != null && !items.isEmpty()) {
            return items;
        }
        return productos;
    }

    @Data
    public static class ClienteDto {
        private String nombre;
        private String apellidos;
        private String correo;
        private Long usuarioId;
        private String calle;
        private String departamento;
        private String region;
        private String comuna;
        private String indicaciones;
    }

    @Data
    public static class DireccionDto {
        private String calle;
        private String departamento;
        private String region;
        private String comuna;
        private String indicaciones;
    }

    @Data
    public static class ItemCompraDto {
        private Long productoId;
        private Long id;
        private Integer cantidad;
        private Double precio;

        public Long getProductoId() {
            if (productoId != null) {
                return productoId;
            }
            return id;
        }
    }
}
