package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CandidaturaResponse;
import br.com.senac.msvagas.dto.CreateVagaRequest;
import br.com.senac.msvagas.dto.UpdateVagaRequest;
import br.com.senac.msvagas.dto.UsuarioResponse;
import br.com.senac.msvagas.dto.VagaResponse;
import br.com.senac.msvagas.service.VagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Vagas", description = "Operações para criação, consulta, atualização e exclusão de vagas, além de candidaturas")
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar vaga", description = "Cria uma nova vaga vinculada à empresa do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public VagaResponse criar(@Valid @RequestBody CreateVagaRequest request,
                              @AuthenticationPrincipal Jwt jwt) {
        return vagaService.criar(request, bearer(jwt));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vaga", description = "Atualiza os dados de uma vaga existente pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public VagaResponse atualizar(@PathVariable Long id,
                                  @Valid @RequestBody UpdateVagaRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        return vagaService.atualizar(id, request, bearer(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir vaga", description = "Remove uma vaga pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vaga excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public void deletar(@PathVariable Long id,
                        @AuthenticationPrincipal Jwt jwt) {
        vagaService.deletar(id, bearer(jwt));
    }

    @GetMapping
    @Operation(summary = "Listar vagas por empresa", description = "Retorna todas as vagas vinculadas à empresa informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public List<VagaResponse> buscarVagas(@AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarVagas(bearer(jwt));
    }

    @GetMapping("/empresa/{idEmpresa}")
    @Operation(summary = "Listar vagas por empresa", description = "Retorna todas as vagas vinculadas à empresa informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public List<VagaResponse> buscarPorEmpresa(@PathVariable Long idEmpresa) {
        return vagaService.buscarPorEmpresa(idEmpresa);
    }

    @GetMapping("/minhas-criadas")
    @Operation(summary = "Listar vagas criadas pelo usuário", description = "Retorna as vagas criadas pela empresa do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public List<VagaResponse> buscarMinhasCriadas(@AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarMinhasCriadas(bearer(jwt));
    }

    @GetMapping("/{idVaga}/candidatos")
    @Operation(summary = "Listar candidatos de uma vaga", description = "Retorna os dados completos dos usuários candidatos vinculados à vaga informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de candidatos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada"),
            @ApiResponse(responseCode = "502", description = "Falha ao consultar dados dos candidatos no ms-usuario")
    })
    public List<UsuarioResponse> buscarCandidatosPorVaga(@PathVariable Long idVaga,
                                                         @AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarCandidatosPorVaga(idVaga, bearer(jwt));
    }

    @PostMapping("/{idVaga}/candidaturas")
    @Operation(summary = "Candidatar-se a uma vaga", description = "Registra a candidatura do candidato autenticado para a vaga informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Candidatura realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada"),
            @ApiResponse(responseCode = "409", description = "Candidatura já registrada para esta vaga")
    })
    public ResponseEntity<CandidaturaResponse> candidatar(@PathVariable Long idVaga,
                                                          @AuthenticationPrincipal Jwt jwt) {
        CandidaturaResponse response = vagaService.candidatar(idVaga, bearer(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idVaga}/candidaturas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover candidatura", description = "Remove a candidatura do candidato autenticado para a vaga informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Candidatura removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Candidatura não encontrada")
    })
    public void removerCandidatura(@PathVariable Long idVaga,
                                   @AuthenticationPrincipal Jwt jwt) {
        vagaService.removerCandidatura(idVaga, bearer(jwt));
    }

    @GetMapping("/minhas-candidaturas")
    @Operation(summary = "Listar minhas candidaturas", description = "Retorna as vagas nas quais o candidato autenticado se candidatou.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de candidaturas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public List<VagaResponse> buscarMinhasCandidaturas(@AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarMinhasCandidaturas(bearer(jwt));
    }

    private String bearer(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }
}
