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
@Schema(
    description = "DTO con los datos necesarios para enviar un mensaje o consulta a través del formulario de contacto",
    example = "{\"nombre\": \"Juan Perez\", \"correo\": \"juan.perez@duoc.cl\", \"mensaje\": \"Consulta sobre stock de consolas retro\"}"
)
public class ContactoDto {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Schema(description = "Nombre completo del usuario que envía el mensaje", example = "Juan Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Ingresa un correo válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @Schema(description = "Correo electrónico de contacto del usuario", example = "juan.perez@duoc.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "El mensaje es requerido")
    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    @Schema(description = "Mensaje o consulta sobre productos o servicios de la tienda retro", example = "Consulta sobre stock de consolas retro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;
}
