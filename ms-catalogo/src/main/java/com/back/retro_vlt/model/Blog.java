package com.back.retro_vlt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un artículo informativo, publicación o noticia en el blog de Retro-VLT")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental de la entrada de blog", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 150)
    @Schema(description = "Título principal del artículo de blog", example = "Evolución y gloria de la era dorada de los 16 bits", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @Column(length = 2000, nullable = false)
    @Schema(description = "Contenido completo o cuerpo del artículo de blog", example = "En los años noventa la batalla de consolas entre SNES y Sega Genesis definió a una generación de jugadores...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contenido;

    @Column(name = "fecha_publicacion")
    @Schema(description = "Fecha de publicación del artículo (formato YYYY-MM-DD)", example = "2024-03-15")
    private LocalDate fechaPublicacion;

    @Column(length = 80)
    @Schema(description = "Nombre del autor o redactor del artículo", example = "RetroTeam VLT")
    private String autor;

    @Column(name = "imagen_url", length = 255)
    @Schema(description = "URL de la imagen destacada o cabecera del artículo", example = "https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=800&q=80")
    private String imagenUrl;
}
