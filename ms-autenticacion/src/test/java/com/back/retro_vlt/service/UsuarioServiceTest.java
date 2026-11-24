package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@retrovlt.cl");
        usuario.setNombre("Test");
        usuario.setPassword("123456");
        usuario.setRol("CLIENTE");
    }

    @Test
    @DisplayName("Registro exitoso de usuario nuevo")
    void testRegistrarUsuarioExitoso() {
        when(usuarioRepository.existsByCorreo("test@retrovlt.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.registrar(usuario);

        assertNotNull(resultado);
        assertEquals("test@retrovlt.cl", resultado.getCorreo());
        assertEquals("CLIENTE", resultado.getRol());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Fallo de registro por correo duplicado")
    void testRegistrarUsuarioCorreoDuplicado() {
        when(usuarioRepository.existsByCorreo("test@retrovlt.cl")).thenReturn(true);

        Usuario resultado = usuarioService.registrar(usuario);

        assertNull(resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login exitoso con correo y contraseña correctos")
    void testLoginExitoso() {
        when(usuarioRepository.findByCorreo("test@retrovlt.cl")).thenReturn(usuario);

        Usuario resultado = usuarioService.login("test@retrovlt.cl", "123456");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }
}
