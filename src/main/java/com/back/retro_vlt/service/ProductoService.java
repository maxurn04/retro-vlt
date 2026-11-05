package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CategoriaRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> obtenerTodos(String buscar, Long categoriaId) {
        if (buscar != null && !buscar.isBlank()) {
            return productoRepository.findByNombreContainingIgnoreCase(buscar.trim());
        }
        if (categoriaId != null) {
            return productoRepository.findByCategoriaId(categoriaId);
        }
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> obtenerOfertas() {
        return productoRepository.findByEsOfertaTrue();
    }

    public List<Producto> obtenerCriticos() {
        return productoRepository.findProductosCriticos();
    }

    public Producto guardar(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria cat = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
            producto.setCategoria(cat);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(producto.getNombre());
            existente.setDescripcion(producto.getDescripcion());
            existente.setPrecio(producto.getPrecio());
            existente.setStock(producto.getStock());
            existente.setStockCritico(producto.getStockCritico() != null ? producto.getStockCritico() : 5);
            existente.setEsOferta(producto.getEsOferta() != null ? producto.getEsOferta() : false);
            existente.setPrecioOferta(producto.getPrecioOferta());
            existente.setImagenUrl(producto.getImagenUrl());

            if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
                Categoria cat = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
                existente.setCategoria(cat);
            }
            return productoRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
