package com.back.retro_vlt.repository;

import com.back.retro_vlt.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    Orden findByNumeroOrden(String numeroOrden);

    List<Orden> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Orden> findAllByOrderByFechaDesc();

    @Query("SELECT COALESCE(SUM(o.total), 0.0) FROM Orden o WHERE o.estado = 'PAGADO'")
    Double sumTotalIngresos();
    boolean existsByUsuarioId(Long usuarioId);
}
