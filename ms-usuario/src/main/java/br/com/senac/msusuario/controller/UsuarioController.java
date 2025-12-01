package br.com.senac.msusuario.controller;

import br.com.senac.msusuario.dto.LoginRequestDTO;
import br.com.senac.msusuario.dto.UsuarioDTO;
import br.com.senac.msusuario.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) {
        return this.service.create(usuarioDTO);
    }

    @PutMapping
    public UsuarioDTO update(@RequestBody UsuarioDTO usuarioDTO) {
        return this.service.update(usuarioDTO);
    }

    @GetMapping("/{id}")
    public UsuarioDTO getById(@PathVariable Long id) {
        return this.service.getById(id);
    }

    @GetMapping("/email/{email}")
    public UsuarioDTO getByEmail(@PathVariable String email) {
        return this.service.getByEmail(email);
    }

    @PostMapping("/login")
    public UsuarioDTO getByLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        return service.login(loginRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}
