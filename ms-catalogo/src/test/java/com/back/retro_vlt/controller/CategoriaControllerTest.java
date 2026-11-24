package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    private Categoria cat1;
    private Categoria cat2;

    @BeforeEach
    void setUp() {
        cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNombre("Consolas");
        cat1.setDescripcion("Consolas retro clásicas");
        cat1.setImagenUrl("https://images.unsplash.com/consolas.jpg");

        cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNombre("Juegos");
        cat2.setDescripcion("Cartuchos y juegos retro");
        cat2.setImagenUrl("https://images.unsplash.com/juegos.jpg");
    }

    @Test
    @DisplayName("Listado de categorías disponibles (200 OK)")
    void testListarCategoriasDisponibles() throws Exception {
        when(categoriaService.obtenerTodas()).thenReturn(List.of(cat1, cat2));

        mockMvc.perform(get("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Consolas"))
                .andExpect(jsonPath("$[1].nombre").value("Juegos"));
    }

    @Test
    @DisplayName("Obtención de detalle de categoría por ID (200 OK)")
    void testObtenerCategoriaPorIdExistente() throws Exception {
        when(categoriaService.obtenerPorId(1L)).thenReturn(cat1);

        mockMvc.perform(get("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Consolas"))
                .andExpect(jsonPath("$.descripcion").value("Consolas retro clásicas"));
    }
}
