package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.service.EducacaoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/candidatos/{candidatoId}/educacoes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Educações", description = "Operações para cadastro, consulta, alteração e remoção de formações acadêmicas do candidato")
@RequiredArgsConstructor
public class EducacaoController {

    private final EducacaoService educacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar educação", description = "Adiciona uma nova formação acadêmica ao candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Educação cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public EducacaoDTO cadastrar(@PathVariable Long candidatoId,
                                 @Valid @RequestBody EducacaoDTO educacaoDTO,
                                 @AuthenticationPrincipal Jwt jwt) {
        return educacaoService.cadastrar(candidatoId, educacaoDTO, jwt);
    }

    @PutMapping("/{educacaoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Alterar educação", description = "Atualiza uma formação acadêmica existente do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Educação alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Educação não encontrada")
    })
    public EducacaoDTO alterar(@PathVariable Long candidatoId,
                               @PathVariable Long educacaoId,
                               @Valid @RequestBody EducacaoDTO educacaoDTO,
                               @AuthenticationPrincipal Jwt jwt) {
        return educacaoService.alterar(candidatoId, educacaoId, educacaoDTO, jwt);
    }

    @GetMapping
    @Operation(summary = "Listar educações do candidato", description = "Retorna todas as formações acadêmicas vinculadas ao candidato informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de educações retornada com sucesso")
    })
    public List<EducacaoDTO> buscar(@PathVariable Long candidatoId) {
        return educacaoService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{educacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir educação", description = "Remove uma formação acadêmica do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Educação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Educação não encontrada")
    })
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long educacaoId,
                        @AuthenticationPrincipal Jwt jwt) {
        educacaoService.deletar(candidatoId, educacaoId, jwt);
    }
}
