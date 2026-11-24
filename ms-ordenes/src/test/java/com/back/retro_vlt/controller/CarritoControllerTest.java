package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.CarritoItemDto;
import com.back.retro_vlt.dto.CarritoItemRequestDto;
import com.back.retro_vlt.service.CarritoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CarritoItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = CarritoItemDto.builder()
                .id(1L)
                .productoId(2L)
                .nombre("Minecraft")
                .precio(1990.0)
                .cantidad(2)
                .subtotal(3980.0)
                .build();
    }

    @Test
    @DisplayName("Obtener ítems del carrito de un usuario (200 OK)")
    void testObtenerCarrito() throws Exception {
        when(carritoService.obtenerItemsPorUsuario(3L)).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/api/carrito/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productoId").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Minecraft"))
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    @DisplayName("Agregar / guardar producto en el carrito (200 OK)")
    void testGuardarItemExitoso() throws Exception {
        CarritoItemRequestDto request = CarritoItemRequestDto.builder()
                .productoId(2L)
                .cantidad(1)
                .build();

        when(carritoService.guardarOSincronizarItem(eq(3L), any(CarritoItemRequestDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/api/carrito/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Minecraft"));
    }
}
