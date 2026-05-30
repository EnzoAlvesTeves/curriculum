package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.service.ExperienciaService;
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
@RequestMapping("/api/candidatos/{candidatoId}/experiencias")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Experiências", description = "Operações para cadastro, consulta, alteração e remoção de experiências profissionais do candidato")
@RequiredArgsConstructor
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar experiência", description = "Adiciona uma nova experiência profissional ao candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Experiência cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public ExperienciaDTO cadastrar(@PathVariable Long candidatoId,
                                    @Valid @RequestBody ExperienciaDTO experienciaDTO,
                                    @AuthenticationPrincipal Jwt jwt) {
        return experienciaService.cadastrar(candidatoId, experienciaDTO, jwt);
    }

    @PutMapping("/{experienciaId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Alterar experiência", description = "Atualiza uma experiência profissional existente do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Experiência alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Experiência não encontrada")
    })
    public ExperienciaDTO alterar(@PathVariable Long candidatoId,
                                  @PathVariable Long experienciaId,
                                  @Valid @RequestBody ExperienciaDTO experienciaDTO,
                                  @AuthenticationPrincipal Jwt jwt) {
        return experienciaService.alterar(candidatoId, experienciaId, experienciaDTO, jwt);
    }

    @GetMapping
    @Operation(summary = "Listar experiências do candidato", description = "Retorna todas as experiências profissionais vinculadas ao candidato informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de experiências retornada com sucesso")
    })
    public List<ExperienciaDTO> buscar(@PathVariable Long candidatoId) {
        return experienciaService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{experienciaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir experiência", description = "Remove uma experiência profissional do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Experiência excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Experiência não encontrada")
    })
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long experienciaId,
                        @AuthenticationPrincipal Jwt jwt) {
        experienciaService.deletar(candidatoId, experienciaId, jwt);
    }
}
