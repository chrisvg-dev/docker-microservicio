package com.cvg.microservicio.usuarios.service;

import com.cvg.microservicio.usuarios.client.CursoClienteRest;
import com.cvg.microservicio.usuarios.models.entity.Usuario;
import com.cvg.microservicio.usuarios.models.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired private CursoClienteRest clienteRest;

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>)this.usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return this.usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return this.usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            return this.usuarioRepository.save(usuario);
        } catch (Exception e) {
            log.error( e.getMessage() );
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        this.usuarioRepository.deleteById(id);
        clienteRest.eliminarCursoUsuario(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return this.usuarioRepository.existsByEmail(email);
    }

    @Override
    public List<Usuario> findAllById(Iterable<Long> ids) {
        return (List<Usuario>) this.usuarioRepository.findAllById(ids);
    }
}
