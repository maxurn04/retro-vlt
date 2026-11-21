package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria(Long id, String nombre) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre(nombre);
        return c;
    }

    @Test
    void noGuardaCategoriaSinNombre() {
        assertThrows(IllegalArgumentException.class, () -> categoriaService.guardar(categoria(null, " ")));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void noGuardaCategoriaConNombreDuplicado() {
        when(categoriaRepository.findByNombreIgnoreCase("Consolas")).thenReturn(categoria(1L, "Consolas"));
        assertThrows(IllegalArgumentException.class, () -> categoriaService.guardar(categoria(null, "Consolas")));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void guardaCategoriaValidaYLimpiaEspacios() {
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));
        Categoria guardada = categoriaService.guardar(categoria(null, " Consolas "));
        assertEquals("Consolas", guardada.getNombre());
    }

    @Test
    void permiteActualizarConsumismoNombre() {
        Categoria existente = categoria(1L, "Consolas");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findByNombreIgnoreCase("Consolas")).thenReturn(existente);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));
        assertNotNull(categoriaService.actualizar(1L, categoria(null, "Consolas")));
    }

    @Test
    void noEliminaCategoriaConProductos() {
        when(productoRepository.existsByCategoriaId(1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> categoriaService.eliminar(1L));
        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    void eliminaCategoriaSinProductos() {
        when(productoRepository.existsByCategoriaId(2L)).thenReturn(false);
        categoriaService.eliminar(2L);
        verify(categoriaRepository).deleteById(2L);
    }

    @Test
    void noEliminaPorNombreUnaCategoriaConProductos() {
        when(categoriaRepository.findByNombreIgnoreCase("Consolas")).thenReturn(categoria(1L, "Consolas"));
        when(productoRepository.existsByCategoriaId(1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> categoriaService.eliminarPorNombre("Consolas"));
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }
}