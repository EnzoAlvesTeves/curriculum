package br.com.senac.msusuario.controller;

import br.com.senac.msusuario.dto.AuthLoginRequest;
import br.com.senac.msusuario.dto.AuthTokenResponse;
import br.com.senac.msusuario.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de autenticação via Keycloak")
public class AuthController {

    private final KeycloakService keycloakService;

    public AuthController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Operation(
            summary = "Login",
            description = "Autentica o usuário com username e senha e retorna os tokens de acesso do Keycloak (JWT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthTokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas", content = @Content)
    })
    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody AuthLoginRequest req) {
        return keycloakService.login(req.username(), req.senha());
    }
}

