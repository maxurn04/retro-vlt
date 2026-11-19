package com.back.retro_vlt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_critico", nullable = false)
    private Integer stockCritico = 5;

    @Column(name = "es_oferta")
    private Boolean esOferta = false;

    @Column(name = "precio_oferta")
    private Double precioOferta;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(name = "img2", length = 255)
    private String img2;

    @Column(name = "img3", length = 255)
    private String img3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
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
