package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario registrar(Usuario usuario) {
        if (usuario == null || usuario.getCorreo() == null) {
            return null;
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return null;
        }
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("CLIENTE");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String correo, String password) {
        if (correo == null || password == null) {
            return null;
        }
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && password.equals(usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(usuario.getNombre());
            existente.setApellidos(usuario.getApellidos());
            existente.setTelefono(usuario.getTelefono());
            existente.setDireccionDefecto(usuario.getDireccionDefecto());
            if (usuario.getRol() != null && !usuario.getRol().isBlank()) {
                existente.setRol(usuario.getRol());
            }
            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                existente.setPassword(usuario.getPassword());
            }
            return usuarioRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
