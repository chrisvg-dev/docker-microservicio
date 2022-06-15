package com.cvg.microservicio.usuarios.models.repository;

import com.cvg.microservicio.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}
