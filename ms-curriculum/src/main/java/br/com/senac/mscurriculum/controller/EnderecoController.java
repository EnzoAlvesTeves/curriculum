package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.service.EnderecoService;
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
@RequestMapping("/api/candidatos/{candidatoId}/endereco")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Endereço", description = "Operações para cadastro, consulta, alteração e remoção do endereço do candidato")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar endereço", description = "Cria o endereço vinculado ao candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public EnderecoDTO cadastrar(@PathVariable Long candidatoId,
                                 @Valid @RequestBody EnderecoDTO enderecoDTO,
                                 @AuthenticationPrincipal Jwt jwt) {
        return enderecoService.cadastrar(candidatoId, enderecoDTO, jwt);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Alterar endereço", description = "Atualiza o endereço do candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public EnderecoDTO alterar(@PathVariable Long candidatoId,
                               @Valid @RequestBody EnderecoDTO enderecoDTO,
                               @AuthenticationPrincipal Jwt jwt) {
        return enderecoService.alterar(candidatoId, enderecoDTO, jwt);
    }

    @GetMapping
    @Operation(summary = "Buscar endereço do candidato", description = "Retorna o endereço vinculado ao candidato informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public EnderecoDTO buscar(@PathVariable Long candidatoId) {
        return enderecoService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir endereço", description = "Remove o endereço vinculado ao candidato autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public void deletar(@PathVariable Long candidatoId,
                        @AuthenticationPrincipal Jwt jwt) {
        enderecoService.deletar(candidatoId, jwt);
    }
}
