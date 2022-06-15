package com.cvg.microservicio.usuarios.service;

import com.cvg.microservicio.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Usuario save(Usuario usuario);
    void deleteUser(Long id);
    Boolean existsByEmail(String email);
}
