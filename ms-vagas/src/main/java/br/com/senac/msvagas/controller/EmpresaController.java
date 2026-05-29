package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CreateEmpresaRequest;
import br.com.senac.msvagas.dto.EmpresaResponse;
import br.com.senac.msvagas.dto.UpdateEmpresaRequest;
import br.com.senac.msvagas.service.EmpresaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaResponse criar(@Valid @RequestBody CreateEmpresaRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return empresaService.criar(request, bearer(jwt));
    }

    @GetMapping
    public List<EmpresaResponse> listar() {
        return empresaService.listar();
    }

    @GetMapping("/{id}")
    public EmpresaResponse buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public EmpresaResponse atualizar(@PathVariable Long id,
                                     @Valid @RequestBody UpdateEmpresaRequest request,
                                     @AuthenticationPrincipal Jwt jwt) {
        return empresaService.atualizar(id, request, bearer(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        empresaService.excluir(id, bearer(jwt));
    }

    private String bearer(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }
}

