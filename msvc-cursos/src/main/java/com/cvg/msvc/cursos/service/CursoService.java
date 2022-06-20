package com.cvg.msvc.cursos.service;

import com.cvg.msvc.cursos.dto.Usuario;
import com.cvg.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> findAll();
    Optional<Curso> findById(Long id);
    Optional<Curso> porIdConUsuarios(Long id);
    Curso save(Curso curso);
    void deleteById(Long id);

    /**
     * CONEXION CON EL MICROSERVICIO USUARIOS
     * @param usuario
     * @param cursoId
     * @return
     */
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
