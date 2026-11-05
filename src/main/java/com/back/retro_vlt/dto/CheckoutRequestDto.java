package com.back.retro_vlt.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequestDto {

    private ClienteDto cliente;
    private DireccionDto direccion;
    private List<ItemCheckoutDto> items;

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
    public static class ItemCheckoutDto {
        private Long productoId;
        private Integer cantidad;
    }
}
