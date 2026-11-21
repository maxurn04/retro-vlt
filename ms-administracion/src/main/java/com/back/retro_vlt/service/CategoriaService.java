package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.repository.CategoriaRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria obtenerPorNombre(String nombre) {
        if (nombre == null) return null;
        return categoriaRepository.findByNombreIgnoreCase(nombre.trim());
    }

    public Categoria guardar(Categoria categoria) {
        validarNombre(categoria.getNombre(), null);
        categoria.setNombre(categoria.getNombre().trim());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = obtenerPorId(id);
        if (existente != null) {
            validarNombre(categoria.getNombre(), id);
            existente.setNombre(categoria.getNombre().trim());
            existente.setDescripcion(categoria.getDescripcion());
            existente.setImagenUrl(categoria.getImagenUrl());
            return categoriaRepository.save(existente);
        }
        return null;
    }

    public Categoria actualizarPorNombre(String nombre, Categoria categoria) {
        Categoria existente = obtenerPorNombre(nombre);
        if (existente != null) {
            validarNombre(categoria.getNombre(), existente.getId());
            existente.setNombre(categoria.getNombre().trim());
            if (categoria.getDescripcion() != null) {
                existente.setDescripcion(categoria.getDescripcion());
            }
            if (categoria.getImagenUrl() != null) {
                existente.setImagenUrl(categoria.getImagenUrl());
            }
            return categoriaRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id) {
        validarSinProductos(id);
        categoriaRepository.deleteById(id);
    }

    public boolean eliminarPorNombre(String nombre) {
        Categoria c = obtenerPorNombre(nombre);
        if (c != null) {
            validarSinProductos(c.getId());
            categoriaRepository.delete(c);
            return true;
        }
        return false;
    }

    private void validarNombre(String nombre, Long idActual) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        Categoria otra = categoriaRepository.findByNombreIgnoreCase(nombre.trim());
        if (otra != null && !otra.getId().equals(idActual)) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre " + nombre.trim());
        }
    }

    private void validarSinProductos(Long categoriaId) {
        if (productoRepository.existsByCategoriaId(categoriaId)) {
            throw new IllegalArgumentException("No se puede eliminar la categoría porque tiene productos asociados");
        }
    }
}