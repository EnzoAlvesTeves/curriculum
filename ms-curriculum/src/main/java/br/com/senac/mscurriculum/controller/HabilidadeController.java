package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.service.HabilidadeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequiredArgsConstructor
public class HabilidadeController {

    private final HabilidadeService habilidadeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabilidadeDTO cadastrar(@PathVariable Long candidatoId,
                                   @Valid @RequestBody HabilidadeDTO habilidadeDTO,
                                   @AuthenticationPrincipal Jwt jwt) {
        return habilidadeService.cadastrar(candidatoId, habilidadeDTO, jwt);
    }

    @PutMapping("/{habilidadeId}")
    @ResponseStatus(HttpStatus.OK)
    public HabilidadeDTO alterar(@PathVariable Long candidatoId,
                                 @PathVariable Long habilidadeId,
                                 @Valid @RequestBody HabilidadeDTO habilidadeDTO,
                                 @AuthenticationPrincipal Jwt jwt) {
        return habilidadeService.alterar(candidatoId, habilidadeId, habilidadeDTO, jwt);
    }

    @GetMapping
    public List<HabilidadeDTO> buscarPorCandidato(@PathVariable Long candidatoId) {
        return habilidadeService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{habilidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long habilidadeId,
                        @AuthenticationPrincipal Jwt jwt) {
        habilidadeService.deletar(candidatoId, habilidadeId, jwt);
    }
}
