package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioClient service;

    public UsuarioController(UsuarioClient service) {
        this.service = service;
    }

    @PostMapping
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            return this.service.create(usuarioDTO);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public UsuarioDTO update(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            return this.service.update(usuarioDTO);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public UsuarioDTO getById(@PathVariable Long id) {
        try {
            return this.service.getById(id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        try {
            this.service.delete(id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
