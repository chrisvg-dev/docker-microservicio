package com.cvg.msvc.cursos.service;

import com.cvg.msvc.cursos.client.UsuarioRestClient;
import com.cvg.msvc.cursos.dto.Usuario;
import com.cvg.msvc.cursos.models.entity.Curso;
import com.cvg.msvc.cursos.models.entity.CursoUsuario;
import com.cvg.msvc.cursos.models.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {
    private final UsuarioRestClient restClient;
    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository, UsuarioRestClient restClient) {
        this.cursoRepository = cursoRepository;
        this.restClient = restClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findAll() {
        return (List<Curso>)this.cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findById(Long id) {
        return this.cursoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = cursoRepository.findById(id);
        if (o.isPresent()) {
            Curso curso = o.orElseThrow();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream()
                        .map(CursoUsuario::getUsuarioId)
                        .collect(Collectors.toList());

                List<Usuario> usuarios = restClient.obtenerAlumnosPorId(ids);
                curso.setUsuarios( usuarios );
            }
            return Optional.of( curso );
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso save(Curso curso) {
        return this.cursoRepository.save( curso );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = this.cursoRepository.findById( cursoId );

        if (curso.isPresent()){
            Usuario usuarioMicroServicio = this.restClient.findById( usuario.getId() );

            Curso c = curso.orElseThrow();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioMicroServicio.getId() );

            c.agregarCursoUsuario( cursoUsuario );
            cursoRepository.save( c );
            return Optional.of( usuarioMicroServicio );
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = this.cursoRepository.findById( cursoId );

        if (curso.isPresent()){
            Usuario usuarioMicroServicio = this.restClient.save( usuario );

            Curso c = curso.orElseThrow();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioMicroServicio.getId() );

            c.agregarCursoUsuario( cursoUsuario );
            cursoRepository.save( c );
            return Optional.of( usuarioMicroServicio );
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> curso = this.cursoRepository.findById( cursoId );

        if (curso.isPresent()){
            Usuario usuarioMicroServicio = this.restClient.findById( usuario.getId() );

            Curso c = curso.orElseThrow();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioMicroServicio.getId() );

            c.eliminarCursoUsuario( cursoUsuario );
            cursoRepository.save( c );
            return Optional.of( usuarioMicroServicio );
        }
        return Optional.empty();
    }
}
