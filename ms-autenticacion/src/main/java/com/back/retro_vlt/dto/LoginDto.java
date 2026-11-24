package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Credenciales para inicio de sesión en el sistema Retro-VLT")
public class LoginDto {

    @Schema(description = "Correo electrónico registrado del usuario", example = "admin@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Schema(description = "Contraseña de acceso", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    public String getContra() {
        return password;
    }

    public void setContra(String contra) {
        this.password = contra;
    }
}
