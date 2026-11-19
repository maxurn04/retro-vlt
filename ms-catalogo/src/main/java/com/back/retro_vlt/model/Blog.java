package com.back.retro_vlt.model;

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
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 2000, nullable = false)
    private String contenido;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(length = 80)
    private String autor;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;
}
