package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CandidaturaResponse;
import br.com.senac.msvagas.dto.CreateVagaRequest;
import br.com.senac.msvagas.dto.UpdateVagaRequest;
import br.com.senac.msvagas.dto.VagaResponse;
import br.com.senac.msvagas.service.VagaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VagaResponse criar(@Valid @RequestBody CreateVagaRequest request,
                              @AuthenticationPrincipal Jwt jwt) {
        return vagaService.criar(request, bearer(jwt));
    }

    @PutMapping("/{id}")
    public VagaResponse atualizar(@PathVariable Long id,
                                  @Valid @RequestBody UpdateVagaRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        return vagaService.atualizar(id, request, bearer(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id,
                        @AuthenticationPrincipal Jwt jwt) {
        vagaService.deletar(id, bearer(jwt));
    }

    @GetMapping("/empresa/{idEmpresa}")
    public List<VagaResponse> buscarPorEmpresa(@PathVariable Long idEmpresa) {
        return vagaService.buscarPorEmpresa(idEmpresa);
    }

    @GetMapping("/minhas-criadas")
    public List<VagaResponse> buscarMinhasCriadas(@AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarMinhasCriadas(bearer(jwt));
    }

    @PostMapping("/{idVaga}/candidaturas")
    public ResponseEntity<CandidaturaResponse> candidatar(@PathVariable Long idVaga,
                                                          @AuthenticationPrincipal Jwt jwt) {
        CandidaturaResponse response = vagaService.candidatar(idVaga, bearer(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idVaga}/candidaturas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerCandidatura(@PathVariable Long idVaga,
                                   @AuthenticationPrincipal Jwt jwt) {
        vagaService.removerCandidatura(idVaga, bearer(jwt));
    }

    @GetMapping("/minhas-candidaturas")
    public List<VagaResponse> buscarMinhasCandidaturas(@AuthenticationPrincipal Jwt jwt) {
        return vagaService.buscarMinhasCandidaturas(bearer(jwt));
    }

    private String bearer(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }
}

