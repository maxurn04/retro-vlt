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
@Schema(description = "Entidad que representa un usuario del sistema Retro-VLT")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 20)
    @Schema(description = "RUT / RUN chileno con formato y dígito verificador", example = "19876543-2")
    private String run;

    @Column(nullable = false, length = 80)
    @Schema(description = "Nombre o primer nombre del usuario", example = "Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 80)
    @Schema(description = "Apellidos del usuario", example = "Mendoza")
    private String apellidos;

    @Column(nullable = false, unique = true, length = 120)
    @Schema(description = "Correo electrónico único de acceso", example = "cliente@duoc.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Column(nullable = false, length = 255)
    @Schema(description = "Contraseña de acceso (almacenada para verificación)", example = "Pass123*", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Column(nullable = false, length = 20)
    @Schema(description = "Rol del usuario en la plataforma (ADMIN o CLIENTE)", example = "CLIENTE", defaultValue = "CLIENTE")
    private String rol = "CLIENTE";

    @Column(length = 20)
    @Schema(description = "Número telefónico móvil de contacto", example = "+56987654321")
    private String telefono;

    @Column(name = "fecha_nacimiento", length = 30)
    @Schema(description = "Fecha de nacimiento en formato YYYY-MM-DD", example = "1998-05-20")
    private String fechaNacimiento;

    @Column(length = 100)
    @Schema(description = "Región de residencia en Chile", example = "Región Metropolitana de Santiago")
    private String region;

    @Column(length = 100)
    @Schema(description = "Comuna de residencia", example = "Las Condes")
    private String comuna;

    @Column(name = "direccion_defecto", length = 255)
    @Schema(description = "Dirección de despacho por defecto", example = "Av. Las Condes 1234, Depto 401")
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
