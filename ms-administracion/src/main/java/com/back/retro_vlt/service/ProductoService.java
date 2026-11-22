package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CategoriaRepository;
import com.back.retro_vlt.repository.DetalleOrdenRepository;
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
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

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

    public List<Producto> obtenerRelacionados(Long id) {
        Producto actual = obtenerPorId(id);
        if (actual != null && actual.getCategoria() != null && actual.getCategoria().getId() != null) {
            List<Producto> lista = productoRepository.findByCategoriaId(actual.getCategoria().getId());
            return lista.stream()
                    .filter(p -> !p.getId().equals(id))
                    .limit(4)
                    .toList();
        }
        return List.of();
    }

    public List<Producto> obtenerOfertas() {
        return productoRepository.findByEsOfertaTrue();
    }

    public List<Producto> obtenerCriticos() {
        return productoRepository.findProductosCriticos();
    }

    public Producto guardar(Producto producto) {
        validar(producto, null);
        if (producto.getStockCritico() == null) {
            producto.setStockCritico(5);
        }
        if (producto.getEsOferta() == null) {
            producto.setEsOferta(false);
        }
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria cat = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
            producto.setCategoria(cat);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtenerPorId(id);
        if (existente != null) {
            validar(producto, id);
            existente.setNombre(producto.getNombre());
            existente.setNombre(producto.getNombre());
            existente.setDescripcion(producto.getDescripcion());
            existente.setPrecio(producto.getPrecio());
            existente.setStock(producto.getStock());
            existente.setStockCritico(producto.getStockCritico() != null ? producto.getStockCritico() : 5);
            existente.setEsOferta(producto.getEsOferta() != null ? producto.getEsOferta() : false);
            existente.setPrecioOferta(producto.getPrecioOferta());
            existente.setImagenUrl(producto.getImagenUrl());
            if (producto.getCodigo() != null) {
                existente.setCodigo(producto.getCodigo());
            }
            if (producto.getImg2() != null) {
                existente.setImg2(producto.getImg2());
            }
            if (producto.getImg3() != null) {
                existente.setImg3(producto.getImg3());
            }

            if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
                Categoria cat = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
                existente.setCategoria(cat);
            }
            return productoRepository.save(existente);
        }
        return null;
    }

    
     public void eliminar(Long id) {
        if (detalleOrdenRepository.existsByProductoId(id)) {
            throw new IllegalArgumentException("No se puede eliminar el producto porque está asociado a órdenes");
        }
        productoRepository.deleteById(id);
    }
       
    private void validar(Producto p, Long idActual) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (p.getPrecio() == null || p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (p.getStock() == null || p.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (p.getStockCritico() != null && p.getStockCritico() < 0) {
            throw new IllegalArgumentException("El stock crítico no puede ser negativo");
        }
        if (Boolean.TRUE.equals(p.getEsOferta())
                && (p.getPrecioOferta() == null || p.getPrecioOferta() <= 0 || p.getPrecioOferta() >= p.getPrecio())) {
            throw new IllegalArgumentException("El precio de oferta debe ser mayor a 0 y menor al precio normal");
        }
        if (p.getCodigo() != null && !p.getCodigo().isBlank()) {
            boolean duplicado = idActual == null
                    ? productoRepository.existsByCodigo(p.getCodigo())
                    : productoRepository.existsByCodigoAndIdNot(p.getCodigo(), idActual);
            if (duplicado) {
                throw new IllegalArgumentException("Ya existe un producto con el código " + p.getCodigo());
            }
        }
        if (p.getCategoria() != null && p.getCategoria().getId() != null
                && !categoriaRepository.existsById(p.getCategoria().getId())) {
            throw new IllegalArgumentException("La categoría indicada no existe");
        }
    }
}
