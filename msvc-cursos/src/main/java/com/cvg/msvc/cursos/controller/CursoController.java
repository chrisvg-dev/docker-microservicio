package com.cvg.msvc.cursos.controller;

import com.cvg.msvc.cursos.models.entity.Curso;
import com.cvg.msvc.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        } );
        return ResponseEntity.badRequest().body(errores);
    }
}
