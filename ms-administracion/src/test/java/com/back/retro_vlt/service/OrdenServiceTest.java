package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.repository.OrdenRepository;
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
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Orden orden(String estado) {
        Orden o = new Orden();
        o.setId(1L);
        o.setEstado(estado);
        return o;
    }

    private Orden cambio(String estado) {
        Orden o = new Orden();
        o.setEstado(estado);
        return o;
    }

    @Test
    void ordenPagadaPuedeDespacharse() {
        Orden existente = orden("PAGADO");
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ordenRepository.save(existente)).thenReturn(existente);
        assertEquals("DESPACHADO", ordenService.actualizar(1L, cambio("DESPACHADO")).getEstado());
    }

    @Test
    void ordenPagadaNoPuedePasarDirectoAEntregada() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden("PAGADO")));
        assertThrows(IllegalArgumentException.class, () -> ordenService.actualizar(1L, cambio("ENTREGADO")));
        verify(ordenRepository, never()).save(any());
    }

    @Test
    void ordenEntregadaNoPuedeCancelarse() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden("ENTREGADO")));
        assertThrows(IllegalArgumentException.class, () -> ordenService.actualizar(1L, cambio("CANCELADO")));
    }

    @Test
    void rechazaEstadoInexistente() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden("PAGADO")));
        assertThrows(IllegalArgumentException.class, () -> ordenService.actualizar(1L, cambio("XYZ")));
    }

    @Test
    void rechazaTotalNegativo() {
        Orden datos = cambio(null);
        datos.setTotal(-1.0);
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden("PAGADO")));
        assertThrows(IllegalArgumentException.class, () -> ordenService.actualizar(1L, datos));
    }

    @Test
    void actualizarOrdenInexistenteDevuelveNulo() {
        when(ordenRepository.findById(9L)).thenReturn(Optional.empty());
        assertNull(ordenService.actualizar(9L, cambio("PAGADO")));
    }

    @Test
    void noEliminaOrdenPagada() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden("PAGADO")));
        assertThrows(IllegalArgumentException.class, () -> ordenService.eliminar(1L));
        verify(ordenRepository, never()).delete(any(Orden.class));
    }

    @Test
    void eliminaOrdenCancelada() {
        Orden existente = orden("CANCELADO");
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(existente));
        assertTrue(ordenService.eliminar(1L));
        verify(ordenRepository).delete(existente);
    }

    @Test
    void eliminaOrdenPendienteDePago() {
        Orden existente = orden("PENDIENTE_PAGO");
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(existente));
        assertTrue(ordenService.eliminar(1L));
        verify(ordenRepository).delete(existente);
    }
}