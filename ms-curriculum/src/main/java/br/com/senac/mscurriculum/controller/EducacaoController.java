package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.service.EducacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos/{candidatoId}/educacoes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EducacaoController {

    private final EducacaoService educacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EducacaoDTO cadastrar(@PathVariable Long candidatoId,
                                 @Valid @RequestBody EducacaoDTO educacaoDTO,
                                 @AuthenticationPrincipal Jwt jwt) {
        return educacaoService.cadastrar(candidatoId, educacaoDTO, jwt);
    }

    @PutMapping("/{educacaoId}")
    @ResponseStatus(HttpStatus.OK)
    public EducacaoDTO alterar(@PathVariable Long candidatoId,
                               @PathVariable Long educacaoId,
                               @Valid @RequestBody EducacaoDTO educacaoDTO,
                               @AuthenticationPrincipal Jwt jwt) {
        return educacaoService.alterar(candidatoId, educacaoId, educacaoDTO, jwt);
    }

    @GetMapping
    public List<EducacaoDTO> buscar(@PathVariable Long candidatoId) {
        return educacaoService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{educacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long educacaoId,
                        @AuthenticationPrincipal Jwt jwt) {
        educacaoService.deletar(candidatoId, educacaoId, jwt);
    }
}
