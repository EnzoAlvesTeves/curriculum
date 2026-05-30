package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.service.EnderecoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatos/{candidatoId}/endereco")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoDTO cadastrar(@PathVariable Long candidatoId,
                               @Valid @RequestBody EnderecoDTO enderecoDTO,
                               @AuthenticationPrincipal Jwt jwt) {
        return enderecoService.cadastrar(candidatoId, enderecoDTO, jwt);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EnderecoDTO alterar(@PathVariable Long candidatoId,
                               @Valid @RequestBody EnderecoDTO enderecoDTO,
                               @AuthenticationPrincipal Jwt jwt) {
        return enderecoService.alterar(candidatoId, enderecoDTO, jwt);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long candidatoId,
                        @AuthenticationPrincipal Jwt jwt) {
        enderecoService.deletar(candidatoId, jwt);
    }
}
