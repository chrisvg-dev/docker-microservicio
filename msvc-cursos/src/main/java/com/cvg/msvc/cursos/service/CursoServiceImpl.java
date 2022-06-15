package com.cvg.msvc.cursos.service;

import com.cvg.msvc.cursos.models.entity.Curso;
import com.cvg.msvc.cursos.models.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
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
    @Transactional
    public Curso save(Curso curso) {
        return this.cursoRepository.save( curso );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.cursoRepository.deleteById(id);
    }
}
