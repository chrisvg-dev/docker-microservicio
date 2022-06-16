package com.cvg.msvc.cursos.controller;

import com.cvg.msvc.cursos.dto.Usuario;
import com.cvg.msvc.cursos.models.entity.Curso;
import com.cvg.msvc.cursos.service.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/cursos")
@CrossOrigin(originPatterns = "*")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(this.cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> findById(@PathVariable Long id) {
        Optional<Curso> curso = this.cursoService.findById(id);
        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) return validate(result);

        Curso curso1 = this.cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) return validate(result);

        Optional<Curso> c = this.cursoService.findById(id);

        if ( c.isPresent() ) {
            Curso cursoDb = c.orElseThrow();
            cursoDb.setNombre( curso.getNombre() );
            return ResponseEntity.status(HttpStatus.CREATED).body( this.cursoService.save(cursoDb) );
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Curso> c = this.cursoService.findById(id);
        if (c.isPresent()) {
            this.cursoService.deleteById( c.orElseThrow().getId() );
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuarioCurso(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
            usuarioOptional = this.cursoService.asignarUsuario( usuario, cursoId );
        } catch (FeignException e){
            String message = String.format("Error al conectarse con el microservicio -> %s", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body( Collections.singletonMap("message", message) );
        }
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body( usuarioOptional.orElseThrow() );
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuarioCurso(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
            usuarioOptional = this.cursoService.crearUsuario( usuario, cursoId );
        } catch (FeignException e){
            String message = String.format("No se pudo crear al usuario debido a un error en la comunicación -> %s", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body( Collections.singletonMap("message", message) );
        }
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body( usuarioOptional.orElseThrow() );
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuarioCurso(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
            usuarioOptional = this.cursoService.eliminarUsuario( usuario, cursoId );
        } catch (FeignException e){
            String message = String.format("Error al conectarse con el microservicio -> %s", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body( Collections.singletonMap("message", message) );
        }
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body( usuarioOptional.orElseThrow() );
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errores);
    }
}
