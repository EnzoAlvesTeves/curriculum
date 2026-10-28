package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.AlterarCandidatoRequest;
import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.service.CandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Candidatos", description = "Operações para cadastro, alteração, consulta e exclusão de candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar candidato", description = "Cria o cadastro completo do candidato autenticado, incluindo endereço e listas relacionadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Candidato cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "409", description = "Candidato já cadastrado para este usuário")
    })
    public CandidatoDTO cadastrar(@Valid @RequestBody CandidatoDTO candidato,
                                  @AuthenticationPrincipal Jwt jwt) {
        return candidatoService.cadastrar(candidato, jwt);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Alterar candidato", description = "Atualiza os dados do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidato alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    public CandidatoDTO alterar(@Valid @RequestBody AlterarCandidatoRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        return candidatoService.alterar(request, jwt);
    }

    @GetMapping("/{candidatoId}")
    @Operation(summary = "Buscar candidato por ID", description = "Retorna o candidato com endereço, habilidades, experiências e educações.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidato encontrado"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    public CandidatoDTO buscar(@PathVariable Long candidatoId) {
        return candidatoService.buscarPorId(candidatoId);
    }

    @GetMapping("/me")
    @Operation(summary = "Buscar candidato autenticado", description = "Retorna o candidato com endereço, habilidades, experiências e educações.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidato encontrado"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    public CandidatoDTO me(@AuthenticationPrincipal Jwt jwt) {
        return candidatoService.me(jwt);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir candidato", description = "Remove o candidato autenticado e seus vínculos associados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Candidato excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    public void deletar(@AuthenticationPrincipal Jwt jwt) {
        candidatoService.deletar(jwt);
    }
}
