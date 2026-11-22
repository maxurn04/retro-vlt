package com.back.retro_vlt.contact.service;

import com.back.retro_vlt.contact.dto.ContactoDto;
import com.back.retro_vlt.contact.model.MensajeContacto;
import com.back.retro_vlt.contact.repository.MensajeContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactoService {

    @Autowired
    private MensajeContactoRepository mensajeContactoRepository;

    public MensajeContacto guardar(ContactoDto dto) {
        MensajeContacto mensaje = new MensajeContacto();
        mensaje.setNombre(dto.getNombre().trim());
        mensaje.setCorreo(dto.getCorreo().trim().toLowerCase());
        mensaje.setMensaje(dto.getMensaje().trim());
        mensaje.setFechaEnvio(LocalDateTime.now());
        return mensajeContactoRepository.save(mensaje);
    }

    public List<MensajeContacto> listar() {
        return mensajeContactoRepository.findAllByOrderByFechaEnvioDesc();
    }
}