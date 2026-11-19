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
@Schema(description = "Entidad que representa un producto del catálogo retro disponible en la tienda")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 50)
    @Schema(description = "Código SKU único o referencia comercial del producto", example = "NES-001")
    private String codigo;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre comercial del producto retro", example = "Nintendo Entertainment System (NES)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 1000)
    @Schema(description = "Descripción detallada del estado físico, compatibilidad y especificaciones", example = "Consola clásica de 8 bits restaurada en perfecto estado con dos mandos originales.")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Precio regular de venta en pesos chilenos (CLP)", example = "89990.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Column(nullable = false)
    @Schema(description = "Cantidad total de unidades disponibles en stock", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @Column(name = "stock_critico", nullable = false)
    @Schema(description = "Umbral mínimo de unidades antes de considerarse stock crítico para reposición", example = "5")
    private Integer stockCritico = 5;

    @Column(name = "es_oferta")
    @Schema(description = "Indica si el producto está actualmente con descuento u oferta promocional", example = "true")
    private Boolean esOferta = false;

    @Column(name = "precio_oferta")
    @Schema(description = "Precio rebajado especial aplicable durante la oferta en CLP", example = "74990.0")
    private Double precioOferta;

    @Column(name = "imagen_url", length = 255)
    @Schema(description = "URL de la fotografía principal del producto", example = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80")
    private String imagenUrl;

    @Column(name = "img2", length = 255)
    @Schema(description = "URL de fotografía secundaria o ángulo lateral del producto", example = "https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&w=600&q=80")
    private String img2;

    @Column(name = "img3", length = 255)
    @Schema(description = "URL de fotografía terciaria o caja/accesorios del producto", example = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=600&q=80")
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
