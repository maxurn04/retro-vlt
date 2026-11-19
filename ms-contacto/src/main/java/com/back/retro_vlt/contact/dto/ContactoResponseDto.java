package com.back.retro_vlt.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "Respuesta del formulario de contacto tras procesar la solicitud",
    example = "{\"exito\": true, \"mensaje\": \"Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad.\"}"
)
public class ContactoResponseDto {

    @Schema(description = "Indica si el envío fue exitoso", example = "true")
    private boolean exito;

    @Schema(description = "Mensaje informativo para el usuario", example = "Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad.")
    private String mensaje;
}
