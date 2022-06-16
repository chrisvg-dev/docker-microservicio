package com.cvg.msvc.cursos.client;

import com.cvg.msvc.cursos.dto.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioRestClient {

    @GetMapping("/api/usuarios")
    Usuario findAll();

    @GetMapping("/api/usuarios/{id}")
    Usuario findById(@PathVariable Long id);

    @PostMapping("/api/usuarios")
    Usuario save(@RequestBody Usuario usuario);


}
