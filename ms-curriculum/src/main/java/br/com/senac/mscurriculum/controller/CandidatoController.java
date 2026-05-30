package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.AlterarCandidatoRequest;
import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.service.CandidatoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidatoDTO cadastrar(@Valid @RequestBody CandidatoDTO candidato,
                                  @AuthenticationPrincipal Jwt jwt) {
        return candidatoService.cadastrar(candidato, jwt);
    }

    @GetMapping("/{candidatoId}")
    public CandidatoDTO buscar(@PathVariable Long candidatoId) {
        return candidatoService.buscarPorId(candidatoId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CandidatoDTO atualizar(@Valid @RequestBody AlterarCandidatoRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        return candidatoService.alterar(request, jwt);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletar(@AuthenticationPrincipal Jwt jwt) {
        candidatoService.deletar(jwt);
    }
}
