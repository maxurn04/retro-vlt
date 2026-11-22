package com.back.retro_vlt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Entidad para persistencia de un producto en el carrito de compras de un usuario")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de carrito", example = "1")
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "ID del usuario propietario del carrito", example = "2")
    private Long usuarioId;

    @Column(name = "producto_id", nullable = false)
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @Column(nullable = false)
    @Schema(description = "Cantidad de unidades del producto", example = "2")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;
}
