package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.OrdenRepository;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrdenRepository ordenRepository;

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
        if (usuario == null || usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!usuario.getCorreo().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }
        if (usuario.getPassword() == null || usuario.getPassword().length() < 4 || usuario.getPassword().length() > 10) {
            throw new IllegalArgumentException("La contraseña debe tener entre 4 y 10 caracteres");
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + usuario.getCorreo());
        }
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("CLIENTE");
        }
        usuario.setRol(validarRol(usuario.getRol()));
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
        if (existente == null) {
            return null;
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        String nuevoRol = existente.getRol();
        if (usuario.getRol() != null && !usuario.getRol().isBlank()) {
            nuevoRol = validarRol(usuario.getRol());
            if ("ADMIN".equals(existente.getRol()) && !"ADMIN".equals(nuevoRol)
                    && usuarioRepository.countByRol("ADMIN") <= 1) {
                throw new IllegalArgumentException("No se puede quitar el rol al último administrador");
            }
        }
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank() && (usuario.getPassword().length() < 4 || usuario.getPassword().length() > 10)) {
            throw new IllegalArgumentException("La contraseña debe tener entre 4 y 10 caracteres");
        }

        existente.setNombre(usuario.getNombre());
        existente.setApellidos(usuario.getApellidos());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccionDefecto(usuario.getDireccionDefecto());
        existente.setRol(nuevoRol);
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
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            existente.setPassword(usuario.getPassword());
        }
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        Usuario u = obtenerPorId(id);
        if (u != null && "ADMIN".equals(u.getRol()) && usuarioRepository.countByRol("ADMIN") <= 1) {
            throw new IllegalArgumentException("No se puede eliminar al último administrador");
        }
        if (ordenRepository.existsByUsuarioId(id)) {
            throw new IllegalArgumentException("No se puede eliminar un usuario que tiene órdenes asociadas");
        }
        usuarioRepository.deleteById(id);
    }

    private String validarRol(String rol) {
        String r = rol.trim().toUpperCase();
        if (!r.equals("ADMIN") && !r.equals("VENDEDOR") && !r.equals("CLIENTE")) {
            throw new IllegalArgumentException("Rol inválido: " + rol + ". Roles permitidos: ADMIN, VENDEDOR, CLIENTE");
        }
        return r;
    }
}