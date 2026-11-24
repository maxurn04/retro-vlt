package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.LoginDto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.security.JwtUtil;
import com.back.retro_vlt.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Registro exitoso de usuario (201 Created)")
    void testRegistroExitoso() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setId(1L);
        nuevo.setNombre("Carlos");
        nuevo.setCorreo("carlos@retrovlt.cl");
        nuevo.setPassword("Pass1234*");
        nuevo.setRol("CLIENTE");

        when(usuarioService.registrar(any(Usuario.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.correo").value("carlos@retrovlt.cl"))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    @DisplayName("Login estándar exitoso con credenciales correctas (200 OK)")
    void testLoginExitoso() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setCorreo("cliente@retrovlt.cl");
        loginDto.setPassword("Password123!");

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setCorreo("cliente@retrovlt.cl");
        usuario.setNombre("Pedro");
        usuario.setRol("CLIENTE");

        when(usuarioService.login("cliente@retrovlt.cl", "Password123!")).thenReturn(usuario);
        when(jwtUtil.generarToken(usuario)).thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"))
                .andExpect(jsonPath("$.correo").value("cliente@retrovlt.cl"))
                .andExpect(jsonPath("$.nombre").value("Pedro"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    @DisplayName("Fallo de login por contraseña incorrecta (401 Unauthorized)")
    void testLoginFalloPasswordIncorrecta() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setCorreo("cliente@retrovlt.cl");
        loginDto.setPassword("WrongPassword");

        when(usuarioService.login("cliente@retrovlt.cl", "WrongPassword")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales incorrectas"));
    }
}
