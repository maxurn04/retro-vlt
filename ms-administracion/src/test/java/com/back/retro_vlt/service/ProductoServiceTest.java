package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CategoriaRepository;
import com.back.retro_vlt.repository.DetalleOrdenRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoValido() {
        Producto p = new Producto();
        p.setNombre("Cartucho Test");
        p.setPrecio(10000.0);
        p.setStock(5);
        return p;
    }

    @Test
    void noGuardaProductoSinNombre() {
        Producto p = productoValido();
        p.setNombre(" ");
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void noGuardaProductoConPrecioCero() {
        Producto p = productoValido();
        p.setPrecio(0.0);
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
    }

    @Test
    void noGuardaProductoConStockNegativo() {
        Producto p = productoValido();
        p.setStock(-1);
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
    }

    @Test
    void noGuardaOfertaConPrecioMayorAlNormal() {
        Producto p = productoValido();
        p.setEsOferta(true);
        p.setPrecioOferta(20000.0);
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
    }

    @Test
    void noGuardaProductoConCodigoDuplicado() {
        Producto p = productoValido();
        p.setCodigo("ABC");
        when(productoRepository.existsByCodigo("ABC")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void noGuardaProductoConCategoriaInexistente() {
        Producto p = productoValido();
        Categoria c = new Categoria();
        c.setId(99L);
        p.setCategoria(c);
        when(categoriaRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(p));
    }

    @Test
    void guardaProductoValido() {
        Producto p = productoValido();
        when(productoRepository.save(p)).thenReturn(p);
        assertSame(p, productoService.guardar(p));
    }

    @Test
    void noActualizaConCodigoDeOtroProducto() {
        Producto existente = productoValido();
        existente.setId(1L);
        Producto nuevo = productoValido();
        nuevo.setCodigo("XYZ");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.existsByCodigoAndIdNot("XYZ", 1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> productoService.actualizar(1L, nuevo));
    }

    @Test
    void actualizarProductoInexistenteDevuelveNulo() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(productoService.actualizar(99L, productoValido()));
    }

    @Test
    void noEliminaProductoAsociadoAOrdenes() {
        when(detalleOrdenRepository.existsByProductoId(1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> productoService.eliminar(1L));
        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void eliminaProductoSinOrdenes() {
        when(detalleOrdenRepository.existsByProductoId(2L)).thenReturn(false);
        productoService.eliminar(2L);
        verify(productoRepository).deleteById(2L);
    }
}
