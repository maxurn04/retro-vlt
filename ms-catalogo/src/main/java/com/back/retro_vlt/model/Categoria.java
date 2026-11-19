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
@Schema(description = "Entidad que representa una categoría de productos en el catálogo de Retro-VLT")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental de la categoría", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre oficial de la categoría", example = "Consolas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 500)
    @Schema(description = "Descripción de los productos y temática de la categoría", example = "Consolas retro clásicas de 8, 16 y 32 bits originales y restauradas")
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    @Schema(description = "URL de la imagen representativa o banner de la categoría", example = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80")
    private String imagenUrl;
}
