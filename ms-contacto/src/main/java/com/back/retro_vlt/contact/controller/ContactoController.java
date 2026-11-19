package com.back.retro_vlt.contact.controller;

import com.back.retro_vlt.contact.dto.ContactoDto;
import com.back.retro_vlt.contact.dto.ContactoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
@Tag(name = "Tienda - Contacto", description = "Endpoints para el formulario de contacto")
public class ContactoController {

    @PostMapping
    @Operation(summary = "Enviar mensaje o consulta de contacto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mensaje enviado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de contacto inválidos o incompletos")
    })
    public ResponseEntity<?> enviarMensaje(@Valid @RequestBody(required = false) ContactoDto request) {
        if (request == null ||
            request.getNombre() == null || request.getNombre().isBlank() ||
            request.getCorreo() == null || request.getCorreo().isBlank() ||
            request.getMensaje() == null || request.getMensaje().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos (nombre, correo y mensaje) son requeridos"));
        }

        ContactoResponseDto resp = ContactoResponseDto.builder()
                .exito(true)
                .mensaje("Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad.")
                .build();

        return ResponseEntity.ok(resp);
    }
}
