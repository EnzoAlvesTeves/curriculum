package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.service.HabilidadeService;
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
@RequestMapping("/api/candidatos/{candidatoId}/habilidades")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Habilidades", description = "Operações para cadastro, consulta, alteração e remoção de habilidades do candidato")
@RequiredArgsConstructor
public class HabilidadeController {

    private final HabilidadeService habilidadeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar habilidade", description = "Adiciona uma nova habilidade ao candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habilidade cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public HabilidadeDTO cadastrar(@PathVariable Long candidatoId,
                                   @Valid @RequestBody HabilidadeDTO habilidadeDTO,
                                   @AuthenticationPrincipal Jwt jwt) {
        return habilidadeService.cadastrar(candidatoId, habilidadeDTO, jwt);
    }

    @PutMapping("/{habilidadeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Alterar habilidade", description = "Atualiza uma habilidade existente do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habilidade alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Habilidade não encontrada")
    })
    public HabilidadeDTO alterar(@PathVariable Long candidatoId,
                                 @PathVariable Long habilidadeId,
                                 @Valid @RequestBody HabilidadeDTO habilidadeDTO,
                                 @AuthenticationPrincipal Jwt jwt) {
        return habilidadeService.alterar(candidatoId, habilidadeId, habilidadeDTO, jwt);
    }

    @GetMapping
    @Operation(summary = "Listar habilidades do candidato", description = "Retorna todas as habilidades vinculadas ao candidato informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de habilidades retornada com sucesso")
    })
    public List<HabilidadeDTO> buscarPorCandidato(@PathVariable Long candidatoId) {
        return habilidadeService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{habilidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir habilidade", description = "Remove uma habilidade do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habilidade excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Habilidade não encontrada")
    })
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long habilidadeId,
                        @AuthenticationPrincipal Jwt jwt) {
        habilidadeService.deletar(candidatoId, habilidadeId, jwt);
    }
}
