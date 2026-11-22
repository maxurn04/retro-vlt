package com.back.retro_vlt.contact.service;

import com.back.retro_vlt.contact.dto.ContactoDto;
import com.back.retro_vlt.contact.model.MensajeContacto;
import com.back.retro_vlt.contact.repository.MensajeContactoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

    @Mock
    private MensajeContactoRepository mensajeContactoRepository;

    @InjectMocks
    private ContactoService contactoService;

    @Test
    void guardaElMensajeLimpiandoEspaciosYCorreoEnMinusculas() {
        ContactoDto dto = ContactoDto.builder()
                .nombre("  Juan Perez ")
                .correo(" Juan.Perez@DUOC.cl ")
                .mensaje(" Consulta de stock ")
                .build();
        when(mensajeContactoRepository.save(any(MensajeContacto.class))).thenAnswer(inv -> inv.getArgument(0));

        MensajeContacto guardado = contactoService.guardar(dto);

        assertEquals("Juan Perez", guardado.getNombre());
        assertEquals("juan.perez@duoc.cl", guardado.getCorreo());
        assertEquals("Consulta de stock", guardado.getMensaje());
        assertNotNull(guardado.getFechaEnvio());
    }

    @Test
    void listaLosMensajesDelRepositorio() {
        MensajeContacto m = new MensajeContacto();
        m.setNombre("Juan");
        when(mensajeContactoRepository.findAllByOrderByFechaEnvioDesc()).thenReturn(List.of(m));

        assertEquals(1, contactoService.listar().size());
    }
}
