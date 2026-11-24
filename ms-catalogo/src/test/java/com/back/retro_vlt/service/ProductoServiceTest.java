package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CategoriaRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Consolas");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Game Boy Color");
        producto.setPrecio(69990.0);
        producto.setStock(10);
        producto.setCategoria(categoria);
        producto.setEsOferta(false);
    }

    @Test
    @DisplayName("Obtener todos los productos sin filtros")
    void testObtenerTodosSinFiltros() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> lista = productoService.obtenerTodos(null, null);

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Game Boy Color", lista.get(0).getNombre());
    }

    @Test
    @DisplayName("Obtener detalle de producto por ID existente")
    void testObtenerPorIdExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto p = productoService.obtenerPorId(1L);

        assertNotNull(p);
        assertEquals(1L, p.getId());
    }
}
