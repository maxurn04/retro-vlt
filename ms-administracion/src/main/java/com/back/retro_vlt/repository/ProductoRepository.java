package com.back.retro_vlt.repository;

import com.back.retro_vlt.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEsOfertaTrue();

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
        boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockCritico")
    List<Producto> findProductosCriticos();
    boolean existsByCategoriaId(Long categoriaId);
}
