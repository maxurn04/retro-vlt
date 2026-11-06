package com.back.retro_vlt.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraDto {

    private ClienteDto cliente;
    private DireccionDto direccion;
    private List<ItemCompraDto> items;
    private String metodoPago;

    @Data
    public static class ClienteDto {
        private String nombre;
        private String apellidos;
        private String correo;
        private Long usuarioId;
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
        private Integer cantidad;
    }
}
