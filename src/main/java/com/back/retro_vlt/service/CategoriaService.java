package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Categoria;
import com.back.retro_vlt.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

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
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(categoria.getNombre());
            existente.setDescripcion(categoria.getDescripcion());
            existente.setImagenUrl(categoria.getImagenUrl());
            return categoriaRepository.save(existente);
        }
        return null;
    }

    public Categoria actualizarPorNombre(String nombre, Categoria categoria) {
        Categoria existente = obtenerPorNombre(nombre);
        if (existente != null) {
            existente.setNombre(categoria.getNombre());
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
        categoriaRepository.deleteById(id);
    }

    public boolean eliminarPorNombre(String nombre) {
        Categoria c = obtenerPorNombre(nombre);
        if (c != null) {
            categoriaRepository.delete(c);
            return true;
        }
        return false;
    }
}
