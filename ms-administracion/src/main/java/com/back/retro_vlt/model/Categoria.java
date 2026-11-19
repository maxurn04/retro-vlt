package com.back.retro_vlt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad representativa de una categoría de productos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental de la categoría", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre descriptivo de la categoría", example = "Videojuegos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 500)
    @Schema(description = "Descripción general de la categoría", example = "Juegos para PC, PlayStation, Xbox y Nintendo")
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    @Schema(description = "URL de imagen ilustrativa de la categoría", example = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600")
    private String imagenUrl;
}
