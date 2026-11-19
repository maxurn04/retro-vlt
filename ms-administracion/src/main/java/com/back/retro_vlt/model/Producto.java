package com.back.retro_vlt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un producto del catálogo")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental del producto", example = "1")
    private Long id;

    @Column(length = 50)
    @Schema(description = "Código de SKU o referencia interna del producto", example = "PROD-W3")
    private String codigo;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre comercial del producto", example = "The Witcher 3: Wild Hunt", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 1000)
    @Schema(description = "Descripción detallada del producto", example = "Juego de rol y acción en mundo abierto de fantasía oscura.")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Precio regular de venta en CLP", example = "3999.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Column(nullable = false)
    @Schema(description = "Cantidad física disponible en stock", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @Column(name = "stock_critico", nullable = false)
    @Schema(description = "Nivel mínimo de inventario para activar alerta de stock crítico", example = "3")
    private Integer stockCritico = 5;

    @Column(name = "es_oferta")
    @Schema(description = "Indica si el producto está en promoción u oferta especial", example = "false")
    private Boolean esOferta = false;

    @Column(name = "precio_oferta")
    @Schema(description = "Precio rebajado en CLP aplicable si está en oferta", example = "2990.0")
    private Double precioOferta;

    @Column(name = "imagen_url", length = 255)
    @Schema(description = "URL principal de la imagen del producto", example = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500")
    private String imagenUrl;

    @Column(name = "img2", length = 255)
    @Schema(description = "URL de imagen secundaria del producto", example = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800")
    private String img2;

    @Column(name = "img3", length = 255)
    @Schema(description = "URL de imagen terciaria del producto", example = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=1000")
    private String img3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    @Schema(description = "Categoría a la que pertenece el producto")
    private Categoria categoria;

    public String getImg() {
        return imagenUrl;
    }

    public void setImg(String img) {
        this.imagenUrl = img;
    }

    public String getDesc() {
        return descripcion;
    }

    public void setDesc(String desc) {
        this.descripcion = desc;
    }

    public Boolean getOferta() {
        return esOferta;
    }

    public void setOferta(Boolean oferta) {
        this.esOferta = oferta;
    }
}
