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

    public Usuario obtenerPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        return usuarioRepository.findByCorreo(correo);
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

    public Usuario loginOauthGoogle(String correo, String nombre) {
        if (correo == null || correo.isBlank()) {
            return null;
        }
        String correoLimpio = correo.trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByCorreo(correoLimpio);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setCorreo(correoLimpio);
            usuario.setNombre(nombre != null && !nombre.isBlank() ? nombre.trim() : "Usuario Google");
            usuario.setPassword("GOOGLE_OAUTH_ACCOUNT");
            usuario.setRol("CLIENTE");
            usuario.setApellidos("");
            usuario.setTelefono("");
            usuario.setDireccionDefecto("");
            return usuarioRepository.save(usuario);
        } else {
            if (nombre != null && !nombre.isBlank()) {
                usuario.setNombre(nombre.trim());
                usuarioRepository.save(usuario);
            }
        }
        return usuario;
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(usuario.getNombre());
            existente.setApellidos(usuario.getApellidos());
            existente.setTelefono(usuario.getTelefono());
            existente.setDireccionDefecto(usuario.getDireccionDefecto());
            if (usuario.getRun() != null) {
                existente.setRun(usuario.getRun());
            }
            if (usuario.getFechaNacimiento() != null) {
                existente.setFechaNacimiento(usuario.getFechaNacimiento());
            }
            if (usuario.getRegion() != null) {
                existente.setRegion(usuario.getRegion());
            }
            if (usuario.getComuna() != null) {
                existente.setComuna(usuario.getComuna());
            }
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
