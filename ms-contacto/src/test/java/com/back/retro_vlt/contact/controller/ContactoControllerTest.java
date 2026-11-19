package com.back.retro_vlt.contact.controller;

import com.back.retro_vlt.contact.dto.ContactoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactoController.class)
class ContactoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Debe responder 200 OK con exito=true cuando los datos son validos")
    void enviarMensaje_Exitoso() throws Exception {
        ContactoDto request = ContactoDto.builder()
                .nombre("Juan Pérez")
                .correo("juan@gmail.com")
                .mensaje("Consulta sobre stock de Nintendo 64")
                .build();

        mockMvc.perform(post("/api/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.mensaje").value("Mensaje recibido correctamente. Nos pondremos en contacto contigo a la brevedad."));
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el nombre esta vacio")
    void enviarMensaje_NombreVacio() throws Exception {
        ContactoDto request = ContactoDto.builder()
                .nombre("")
                .correo("juan@gmail.com")
                .mensaje("Consulta")
                .build();

        mockMvc.perform(post("/api/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Todos los campos (nombre, correo y mensaje) son requeridos"));
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el correo esta vacio")
    void enviarMensaje_CorreoVacio() throws Exception {
        ContactoDto request = ContactoDto.builder()
                .nombre("Juan")
                .correo("")
                .mensaje("Consulta")
                .build();

        mockMvc.perform(post("/api/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Todos los campos (nombre, correo y mensaje) son requeridos"));
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el correo tiene formato invalido")
    void enviarMensaje_CorreoInvalido() throws Exception {
        ContactoDto request = ContactoDto.builder()
                .nombre("Juan")
                .correo("correo-invalido")
                .mensaje("Consulta")
                .build();

        mockMvc.perform(post("/api/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Todos los campos (nombre, correo y mensaje) son requeridos"));
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el mensaje esta vacio")
    void enviarMensaje_MensajeVacio() throws Exception {
        ContactoDto request = ContactoDto.builder()
                .nombre("Juan")
                .correo("juan@gmail.com")
                .mensaje("")
                .build();

        mockMvc.perform(post("/api/contacto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Todos los campos (nombre, correo y mensaje) son requeridos"));
    }
}
