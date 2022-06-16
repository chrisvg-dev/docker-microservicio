package com.cvg.microservicio.usuarios.controller;

import com.cvg.microservicio.usuarios.models.entity.Usuario;
import com.cvg.microservicio.usuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll(){
        return new ResponseEntity<>( usuarioService.findAll(), HttpStatus.OK );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = this.usuarioService.findById(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(this.usuarioService.findById(id).orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return validate(result);
        }

        Boolean isEmailAvailable = this.usuarioService.existsByEmail( usuario.getEmail() );
        if (isEmailAvailable) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "El email ya está asociado a otro usuario"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body( this.usuarioService.save(usuario) );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return validate(result);
        }

        Boolean isEmailAvailable = this.usuarioService.existsByEmail( usuario.getEmail() );
        if (isEmailAvailable) {
            return ResponseEntity.badRequest().body( Collections.singletonMap("message", "El email ya está asociado a otro usuario") );
        }
        Optional<Usuario> optional = this.usuarioService.findById(id);
        if (optional.isPresent()) {
            Usuario usuarioDb = optional.orElseThrow();
            usuarioDb.setNombre( usuario.getNombre() );
            usuarioDb.setEmail( usuario.getEmail() );
            usuarioDb.setPassword( usuario.getPassword() );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body( this.usuarioService.save(usuarioDb) );
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Usuario> optional = this.usuarioService.findById( id );
        if (optional.isPresent()) {
            this.usuarioService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-cursos")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids ){
        return ResponseEntity.ok( this.usuarioService.findAllById( ids ) );
    }


    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errores);
    }
}
