package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.OrdenRepository;
import com.back.retro_vlt.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioValido() {
        Usuario u = new Usuario();
        u.setNombre("Test");
        u.setCorreo("test@correo.cl");
        u.setPassword("123456");
        return u;
    }

    @Test
    void noRegistraCorreoConFormatoInvalido() {
        Usuario u = usuarioValido();
        u.setCorreo("sin-arroba");
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(u));
    }

    @Test
    void noRegistraPasswordCorta() {
        Usuario u = usuarioValido();
        u.setPassword("123");
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(u));
    }

    @Test
    void noRegistraCorreoDuplicado() {
        when(usuarioRepository.existsByCorreo("test@correo.cl")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(usuarioValido()));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void noRegistraRolInvalido() {
        Usuario u = usuarioValido();
        u.setRol("SUPERMAN");
        when(usuarioRepository.existsByCorreo("test@correo.cl")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(u));
    }

    @Test
    void sinRolAsignaCliente() {
        when(usuarioRepository.existsByCorreo("test@correo.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        Usuario creado = usuarioService.registrar(usuarioValido());
        assertEquals("CLIENTE", creado.getRol());
    }

    @Test
    void normalizaElRolAMayusculas() {
        Usuario u = usuarioValido();
        u.setRol("vendedor");
        when(usuarioRepository.existsByCorreo("test@correo.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        assertEquals("VENDEDOR", usuarioService.registrar(u).getRol());
    }

    @Test
    void noEliminaAlUltimoAdministrador() {
        Usuario admin = new Usuario();
        admin.setId(1L);
        admin.setRol("ADMIN");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(usuarioRepository.countByRol("ADMIN")).thenReturn(1L);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.eliminar(1L));
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void noEliminaUsuarioConOrdenes() {
        Usuario cliente = new Usuario();
        cliente.setId(2L);
        cliente.setRol("CLIENTE");
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(cliente));
        when(ordenRepository.existsByUsuarioId(2L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.eliminar(2L));
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void eliminaUsuarioSinOrdenes() {
        Usuario cliente = new Usuario();
        cliente.setId(3L);
        cliente.setRol("CLIENTE");
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(cliente));
        usuarioService.eliminar(3L);
        verify(usuarioRepository).deleteById(3L);
    }

    @Test
    void noQuitaElRolAlUltimoAdministrador() {
        Usuario admin = new Usuario();
        admin.setId(1L);
        admin.setNombre("Admin");
        admin.setRol("ADMIN");
        Usuario cambios = new Usuario();
        cambios.setNombre("Admin");
        cambios.setRol("CLIENTE");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(usuarioRepository.countByRol("ADMIN")).thenReturn(1L);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.actualizar(1L, cambios));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void noActualizaUsuarioSinNombre() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Existente");
        existente.setRol("CLIENTE");
        Usuario cambios = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        assertThrows(IllegalArgumentException.class, () -> usuarioService.actualizar(1L, cambios));
    }

    @Test
    void noRegistraPasswordDemasiadoLarga() {
        Usuario u = usuarioValido();
        u.setPassword("12345678901");
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(u));
    }
}