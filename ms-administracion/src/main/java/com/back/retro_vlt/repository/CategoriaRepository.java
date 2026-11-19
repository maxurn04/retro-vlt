package com.back.retro_vlt.repository;

import com.back.retro_vlt.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByNombreIgnoreCase(String nombre);
}
