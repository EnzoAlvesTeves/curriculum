package br.com.senac.msusuario.controller;

import br.com.senac.msusuario.dto.AlterarSenhaPorUsernameRequest;
import br.com.senac.msusuario.dto.AlterarSenhaRequest;
import br.com.senac.msusuario.dto.CreateUsuarioRequest;
import br.com.senac.msusuario.dto.UpdateUsuarioRequest;
import br.com.senac.msusuario.dto.UsuarioResponse;
import br.com.senac.msusuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuários", description = "Gerenciamento de usuários da plataforma")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário na plataforma e provisiona a conta no Keycloak."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody CreateUsuarioRequest req) {
        return service.criar(req);
    }

    @Operation(
            summary = "Listar usuários",
            description = "Retorna a lista de todos os usuários cadastrados. Requer autenticação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    @Operation(
            summary = "Dados do usuário autenticado",
            description = "Retorna os dados do usuário correspondente ao token JWT enviado no cabeçalho."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal Jwt jwt) {
        return service.buscarMe(jwt);
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public UsuarioResponse buscar(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados cadastrais de um usuário existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ausentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsuarioRequest req) {
        return service.atualizar(id, req);
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove o usuário da plataforma e revoga o acesso no Keycloak."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {
        service.deletar(id);
    }

    @Operation(
            summary = "Alterar própria senha",
            description = "Valida a senha atual e altera a senha do usuário autenticado no Keycloak."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Senha atual inválida ou dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PatchMapping("/me/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarMinhaSenha(@AuthenticationPrincipal Jwt jwt,
                                  @Valid @RequestBody AlterarSenhaRequest req) {
        service.alterarMinhaSenha(jwt, req);
    }

    @Operation(
            summary = "Alterar senha por username",
            description = "Recebe o username (login/e-mail), busca o keycloakUserId no banco e atualiza a senha no Keycloak."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PatchMapping("/senha/username")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenhaPorUsername(@Valid @RequestBody AlterarSenhaPorUsernameRequest req) {
        service.alterarSenhaPorUsername(req);
    }
}

