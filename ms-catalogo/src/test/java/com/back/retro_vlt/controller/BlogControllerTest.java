package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Blog;
import com.back.retro_vlt.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlogService blogService;

    private Blog blog1;
    private Blog blog2;

    @BeforeEach
    void setUp() {
        blog1 = new Blog();
        blog1.setId(1L);
        blog1.setTitulo("Evolución y gloria de la era dorada de los 16 bits");
        blog1.setContenido("En los años noventa la batalla de consolas...");
        blog1.setFechaPublicacion(LocalDate.of(2024, 3, 15));
        blog1.setAutor("RetroTeam VLT");

        blog2 = new Blog();
        blog2.setId(2L);
        blog2.setTitulo("Guía para conservar y limpiar tus cartuchos de NES");
        blog2.setContenido("El mantenimiento preventivo de contactos de cobre...");
        blog2.setFechaPublicacion(LocalDate.of(2024, 3, 20));
        blog2.setAutor("Staff Técnico");
    }

    @Test
    @DisplayName("Listado de artículos de blog activos (200 OK)")
    void testListarArticulosBlogActivos() throws Exception {
        when(blogService.obtenerTodos()).thenReturn(List.of(blog1, blog2));

        mockMvc.perform(get("/api/blogs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Evolución y gloria de la era dorada de los 16 bits"))
                .andExpect(jsonPath("$[1].titulo").value("Guía para conservar y limpiar tus cartuchos de NES"));
    }

    @Test
    @DisplayName("Obtención de artículo de blog por ID existente (200 OK)")
    void testObtenerArticuloBlogPorIdExistente() throws Exception {
        when(blogService.obtenerPorId(1L)).thenReturn(blog1);

        mockMvc.perform(get("/api/blogs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Evolución y gloria de la era dorada de los 16 bits"))
                .andExpect(jsonPath("$.autor").value("RetroTeam VLT"));
    }
}
