package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.service.ExperienciaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequiredArgsConstructor
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExperienciaDTO cadastrar(@PathVariable Long candidatoId,
                                    @Valid @RequestBody ExperienciaDTO experienciaDTO,
                                    @AuthenticationPrincipal Jwt jwt) {
        return experienciaService.cadastrar(candidatoId, experienciaDTO, jwt);
    }

    @PutMapping("/{experienciaId}")
    @ResponseStatus(HttpStatus.OK)
    public ExperienciaDTO alterar(@PathVariable Long candidatoId,
                                  @PathVariable Long experienciaId,
                                  @Valid @RequestBody ExperienciaDTO experienciaDTO,
                                  @AuthenticationPrincipal Jwt jwt) {
        return experienciaService.alterar(candidatoId, experienciaId, experienciaDTO, jwt);
    }

    @GetMapping
    public List<ExperienciaDTO> buscar(@PathVariable Long candidatoId) {
        return experienciaService.buscarPorCandidato(candidatoId);
    }

    @DeleteMapping("/{experienciaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long candidatoId,
                        @PathVariable Long experienciaId,
                        @AuthenticationPrincipal Jwt jwt) {
        experienciaService.deletar(candidatoId, experienciaId, jwt);
    }
}
