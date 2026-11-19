package com.back.retro_vlt.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para solicitud de contacto")
public class ContactoDto {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Ingresa un correo válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @Schema(description = "Correo electrónico del usuario", example = "juan@gmail.com")
    private String correo;

    @NotBlank(message = "El mensaje es requerido")
    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    @Schema(description = "Mensaje o consulta del usuario", example = "Consulta sobre disponibilidad de Nintendo 64")
    private String mensaje;
}
