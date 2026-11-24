package com.back.retro_vlt.controller;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    private Producto producto1;
    private Producto productoOferta;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Consolas");
        categoria.setDescripcion("Consolas retro clásicas");

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setCodigo("NES-001");
        producto1.setNombre("Nintendo Entertainment System (NES)");
        producto1.setDescripcion("Consola clásica de 8 bits");
        producto1.setPrecio(89990.0);
        producto1.setStock(15);
        producto1.setStockCritico(5);
        producto1.setEsOferta(false);
        producto1.setCategoria(categoria);

        productoOferta = new Producto();
        productoOferta.setId(2L);
        productoOferta.setCodigo("SNES-002");
        productoOferta.setNombre("Super Nintendo (SNES)");
        productoOferta.setDescripcion("Consola clásica de 16 bits");
        productoOferta.setPrecio(119990.0);
        productoOferta.setPrecioOferta(99990.0);
        productoOferta.setStock(8);
        productoOferta.setEsOferta(true);
        productoOferta.setCategoria(categoria);
    }

    @Test
    @DisplayName("Listado completo de productos activos (200 OK)")
    void testListadoCompletoProductosActivos() throws Exception {
        when(productoService.obtenerTodos(null, null)).thenReturn(List.of(producto1, productoOferta));

        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Nintendo Entertainment System (NES)"))
                .andExpect(jsonPath("$[1].nombre").value("Super Nintendo (SNES)"));
    }

    @Test
    @DisplayName("Obtención de detalle de producto por ID existente (200 OK)")
    void testObtenerDetalleProductoPorIdExistente() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(producto1);

        mockMvc.perform(get("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("NES-001"))
                .andExpect(jsonPath("$.nombre").value("Nintendo Entertainment System (NES)"))
                .andExpect(jsonPath("$.precio").value(89990.0))
                .andExpect(jsonPath("$.stock").value(15));
    }
}
