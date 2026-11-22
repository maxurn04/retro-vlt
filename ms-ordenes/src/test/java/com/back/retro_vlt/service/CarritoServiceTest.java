package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.CarritoItemDto;
import com.back.retro_vlt.dto.CarritoItemRequestDto;
import com.back.retro_vlt.model.CarritoItem;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CarritoItemRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Producto producto;
    private CarritoItem carritoItem;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Minecraft");
        producto.setPrecio(1990.0);
        producto.setEsOferta(false);
        producto.setImagenUrl("https://example.com/mc.jpg");

        carritoItem = CarritoItem.builder()
                .id(1L)
                .usuarioId(3L)
                .productoId(2L)
                .cantidad(2)
                .producto(producto)
                .build();
    }

    @Test
    void testObtenerItemsPorUsuario() {
        when(carritoItemRepository.findByUsuarioId(3L)).thenReturn(List.of(carritoItem));

        List<CarritoItemDto> items = carritoService.obtenerItemsPorUsuario(3L);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(2L, items.get(0).getProductoId());
        assertEquals("Minecraft", items.get(0).getNombre());
        assertEquals(1990.0, items.get(0).getPrecio());
        assertEquals(2, items.get(0).getCantidad());
        assertEquals(3980.0, items.get(0).getSubtotal());
    }

    @Test
    void testGuardarItemNuevo() {
        CarritoItemRequestDto request = CarritoItemRequestDto.builder()
                .productoId(2L)
                .cantidad(1)
                .build();

        when(carritoItemRepository.findByUsuarioIdAndProductoId(3L, 2L)).thenReturn(Optional.empty());
        when(carritoItemRepository.save(any(CarritoItem.class))).thenAnswer(invocation -> {
            CarritoItem item = invocation.getArgument(0);
            item.setId(10L);
            item.setProducto(producto);
            return item;
        });

        CarritoItemDto res = carritoService.guardarOSincronizarItem(3L, request);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals(2L, res.getProductoId());
        assertEquals(1, res.getCantidad());
    }

    @Test
    void testGuardarItemExistenteIncrementar() {
        CarritoItemRequestDto request = CarritoItemRequestDto.builder()
                .productoId(2L)
                .cantidad(3)
                .sobrescribir(false)
                .build();

        when(carritoItemRepository.findByUsuarioIdAndProductoId(3L, 2L)).thenReturn(Optional.of(carritoItem));
        when(carritoItemRepository.save(any(CarritoItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoItemDto res = carritoService.guardarOSincronizarItem(3L, request);

        assertNotNull(res);
        assertEquals(5, res.getCantidad()); // 2 + 3 = 5
    }

    @Test
    void testActualizarCantidadExitoso() {
        when(carritoItemRepository.findByUsuarioIdAndProductoId(3L, 2L)).thenReturn(Optional.of(carritoItem));
        when(carritoItemRepository.save(any(CarritoItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoItemDto res = carritoService.actualizarCantidad(3L, 2L, 4, null);

        assertNotNull(res);
        assertEquals(4, res.getCantidad());
    }

    @Test
    void testActualizarCantidadEliminarCuandoCero() {
        when(carritoItemRepository.findByUsuarioIdAndProductoId(3L, 2L)).thenReturn(Optional.of(carritoItem));

        CarritoItemDto res = carritoService.actualizarCantidad(3L, 2L, 0, null);

        assertNull(res);
        verify(carritoItemRepository, times(1)).delete(carritoItem);
    }

    @Test
    void testEliminarItem() {
        when(carritoItemRepository.findByUsuarioIdAndProductoId(3L, 2L)).thenReturn(Optional.of(carritoItem));

        boolean res = carritoService.eliminarItem(3L, 2L);

        assertTrue(res);
        verify(carritoItemRepository, times(1)).deleteByUsuarioIdAndProductoId(3L, 2L);
    }

    @Test
    void testVaciarCarrito() {
        boolean res = carritoService.vaciarCarrito(3L);

        assertTrue(res);
        verify(carritoItemRepository, times(1)).deleteByUsuarioId(3L);
    }
}
