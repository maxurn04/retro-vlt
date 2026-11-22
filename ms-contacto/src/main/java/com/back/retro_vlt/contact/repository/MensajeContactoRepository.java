package com.back.retro_vlt.contact.repository;

import com.back.retro_vlt.contact.model.MensajeContacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    List<MensajeContacto> findAllByOrderByFechaEnvioDesc();
}