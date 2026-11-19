package com.back.retro_vlt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de autenticación federada con Google OAuth2 / Identity Services")
public class GoogleAuthDto {

    @Schema(description = "Token de acceso OAuth o ID Token de Google", example = "ya29.a0AfH6SMBx...")
    private String token;

    @Schema(description = "Credencial JWT Google Sign-In (Credential Response)", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String credential;

    @Schema(description = "Correo electrónico proporcionado por la cuenta de Google", example = "cliente@duoc.cl")
    private String correo;

    @Schema(description = "Nombre completo del usuario según perfil de Google", example = "Carlos Mendoza")
    private String nombre;

    @Schema(description = "URL de foto de perfil proporcionada por Google", example = "https://lh3.googleusercontent.com/a/ACg8ocI...")
    private String picture;
}
