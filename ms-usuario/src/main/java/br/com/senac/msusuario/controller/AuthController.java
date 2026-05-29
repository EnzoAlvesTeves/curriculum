package br.com.senac.msusuario.controller;

import br.com.senac.msusuario.dto.AuthLoginRequest;
import br.com.senac.msusuario.dto.AuthTokenResponse;
import br.com.senac.msusuario.service.KeycloakService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakService keycloakService;

    public AuthController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody AuthLoginRequest req) {
        return keycloakService.login(req.username(), req.senha());
    }
}

