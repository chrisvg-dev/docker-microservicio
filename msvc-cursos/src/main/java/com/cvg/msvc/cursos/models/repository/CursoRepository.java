package com.cvg.msvc.cursos.models.repository;

import com.cvg.msvc.cursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    @Modifying
    @Query("DELETE FROM CursoUsuario cu WHERE cu.usuarioId = :userId")
    void eliminarCursoUsuarioPorId(@Param("userId") Long userId);

}
