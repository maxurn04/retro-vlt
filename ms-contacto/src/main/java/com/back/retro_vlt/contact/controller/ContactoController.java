package com.back.retro_vlt.contact.controller;

import com.back.retro_vlt.contact.model.MensajeContacto;
import com.back.retro_vlt.contact.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.back.retro_vlt.contact.dto.ContactoDto;
import com.back.retro_vlt.contact.dto.ContactoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contacto")
@CrossOrigin(origins = "*")
@Tag(name = "Tienda - Contacto", description = "Endpoints para el formulario de contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping
    @Operation(
        summary = "Enviar mensaje o consulta de contacto",
        description = "Permite a los usuarios enviar un mensaje, consulta sobre stock o comentario general al equipo de Retro VLT mediante el formulario de contacto.",
        requestBody = @RequestBody(
            description = "Datos del formulario de contacto (nombre, correo y mensaje son obligatorios)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContactoDto.class),
                examples = @ExampleObject(
                    name = "EjemploContacto",
                    summary = "Ejemplo funcional de consulta de stock",
                    description = "Datos de prueba listos para Try it out",
                    value = "{\n  \"nombre\": \"Juan Perez\",\n  \"correo\": \"juan.perez@duoc.cl\",\n  \"mensaje\": \"Consulta sobre stock de consolas retro\"\n}"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Mensaje enviado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContactoResponseDto.class),
                examples = @ExampleObject(
                    name = "RespuestaExitosa",
                    summary = "Ejemplo de confirmación de envío",
                    value = "{\n  \"exito\": true,\n  \"mensaje\": \"Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad.\"\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de contacto inválidos o incompletos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "ErrorValidacion",
                    summary = "Ejemplo de error por datos incompletos",
                    value = "{\n  \"error\": \"Todos los campos (nombre, correo y mensaje) son requeridos\"\n}"
                )
            )
        )
    })
    public ResponseEntity<?> enviarMensaje(@Valid @org.springframework.web.bind.annotation.RequestBody(required = false) ContactoDto request) {
        if (request == null ||
            request.getNombre() == null || request.getNombre().isBlank() ||
            request.getCorreo() == null || request.getCorreo().isBlank() ||
            request.getMensaje() == null || request.getMensaje().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos (nombre, correo y mensaje) son requeridos"));
        }
        contactoService.guardar(request);
        ContactoResponseDto resp = ContactoResponseDto.builder()
                .exito(true)
                .mensaje("Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad.")
                .build();

        return ResponseEntity.ok(resp);
    }

    @GetMapping
    @Operation(
        summary = "Listar mensajes de contacto recibidos",
        description = "Devuelve los mensajes guardados, del más reciente al más antiguo. Requiere token JWT de un usuario ADMIN."
    )
    public ResponseEntity<List<MensajeContacto>> listarMensajes() {
        return ResponseEntity.ok(contactoService.listar());
    }
}
