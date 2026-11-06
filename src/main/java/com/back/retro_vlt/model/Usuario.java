package com.back.retro_vlt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String run;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 80)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol = "CLIENTE";

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_nacimiento", length = 30)
    private String fechaNacimiento;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String comuna;

    @Column(name = "direccion_defecto", length = 255)
    private String direccionDefecto;

    // Métodos de conveniencia para compatibilidad directa con el frontend React
    public String getContra() {
        return password;
    }

    public void setContra(String contra) {
        this.password = contra;
    }

    public String getTipo() {
        return rol;
    }

    public void setTipo(String tipo) {
        this.rol = tipo;
    }

    public String getDireccion() {
        return direccionDefecto;
    }

    public void setDireccion(String direccion) {
        this.direccionDefecto = direccion;
    }
}
