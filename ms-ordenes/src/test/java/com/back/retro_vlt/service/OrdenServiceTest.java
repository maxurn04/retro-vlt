package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.CompraDto;
import com.back.retro_vlt.dto.PagoDto;
import com.back.retro_vlt.model.DetalleOrden;
import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.OrdenRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Producto producto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Super Nintendo");
        producto.setPrecio(150000.0);
        producto.setStock(10);
        producto.setEsOferta(false);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@usuario.cl");
        usuario.setNombre("Test");
    }

    @Test
    @DisplayName("Procesar compra exitosa desde el carrito")
    void testProcesarCompraExitoso() {
        CompraDto dto = new CompraDto();
        CompraDto.ClienteDto cliente = new CompraDto.ClienteDto();
        cliente.setUsuarioId(1L);
        dto.setCliente(cliente);

        CompraDto.ItemCompraDto item = new CompraDto.ItemCompraDto();
        item.setProductoId(1L);
        item.setCantidad(2);
        dto.setItems(List.of(item));
        dto.setMetodoPago("WEBPAY");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Orden resultado = ordenService.procesarCompra(dto);

        assertNotNull(resultado);
        assertEquals("PENDIENTE_PAGO", resultado.getEstado());
        assertEquals(300000.0, resultado.getTotal());
        assertEquals("WEBPAY", resultado.getMetodoPago());
        assertEquals(1, resultado.getDetalles().size());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    @DisplayName("Procesar pago exitoso con descuento de stock")
    void testProcesarPagoExitoso() {
        Orden orden = new Orden();
        orden.setId(10L);
        orden.setEstado("PENDIENTE_PAGO");

        DetalleOrden detalle = new DetalleOrden();
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        orden.setDetalles(new ArrayList<>(List.of(detalle)));

        when(ordenRepository.findById(10L)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoDto pagoDto = new PagoDto();
        pagoDto.setSimularError(false);
        pagoDto.setMetodoPago("TARJETA_DEBITO");

        Orden resultado = ordenService.procesarPago(10L, pagoDto);

        assertNotNull(resultado);
        assertEquals("PAGADO", resultado.getEstado());
        assertEquals(8, producto.getStock());
        verify(productoRepository, times(1)).save(producto);
        verify(ordenRepository, times(1)).save(orden);
    }
}
