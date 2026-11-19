package com.back.retro_vlt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un usuario registrado (Cliente o Administrador)")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental del usuario", example = "2")
    private Long id;

    @Column(length = 20)
    @Schema(description = "RUT o RUN del usuario chileno", example = "19.876.543-2")
    private String run;

    @Column(nullable = false, length = 80)
    @Schema(description = "Nombre de pila del usuario", example = "Pedro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 80)
    @Schema(description = "Apellidos del usuario", example = "Hacker")
    private String apellidos;

    @Column(nullable = false, unique = true, length = 120)
    @Schema(description = "Correo electrónico institucional o personal", example = "pedro.hacker20@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Column(nullable = false, length = 255)
    @Schema(description = "Contraseña en texto plano o hash para autenticación", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Column(nullable = false, length = 20)
    @Schema(description = "Rol del usuario en el sistema", example = "CLIENTE", allowableValues = {"CLIENTE", "ADMIN"})
    private String rol = "CLIENTE";

    @Column(length = 20)
    @Schema(description = "Número telefónico de contacto", example = "+56999887766")
    private String telefono;

    @Column(name = "fecha_nacimiento", length = 30)
    @Schema(description = "Fecha de nacimiento en formato YYYY-MM-DD", example = "1998-08-20")
    private String fechaNacimiento;

    @Column(length = 100)
    @Schema(description = "Región de residencia", example = "Región Metropolitana de Santiago")
    private String region;

    @Column(length = 100)
    @Schema(description = "Comuna de residencia", example = "Cerrillos")
    private String comuna;

    @Column(name = "direccion_defecto", length = 255)
    @Schema(description = "Dirección predeterminada de despacho", example = "Los Crisantemos, Edificio Norte, Depto 603, Cerrillos")
    private String direccionDefecto;

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
