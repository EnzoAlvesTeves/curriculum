package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CreateEmpresaRequest;
import br.com.senac.msvagas.dto.EmpresaResponse;
import br.com.senac.msvagas.dto.UpdateEmpresaRequest;
import br.com.senac.msvagas.service.EmpresaService;
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
@RequestMapping("/api/empresas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Empresas", description = "Operações para criação, consulta, atualização e exclusão de empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar empresa", description = "Cria uma nova empresa vinculada ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    })
    public EmpresaResponse criar(@Valid @RequestBody CreateEmpresaRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return empresaService.criar(request, bearer(jwt));
    }

    @GetMapping
    @Operation(summary = "Listar empresas", description = "Retorna todas as empresas cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso")
    })
    public List<EmpresaResponse> listar() {
        return empresaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por ID", description = "Retorna os dados de uma empresa pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public EmpresaResponse buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public EmpresaResponse atualizar(@PathVariable Long id,
                                     @Valid @RequestBody UpdateEmpresaRequest request,
                                     @AuthenticationPrincipal Jwt jwt) {
        return empresaService.atualizar(id, request, bearer(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir empresa", description = "Remove a empresa pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empresa excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        empresaService.excluir(id, bearer(jwt));
    }

    private String bearer(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }
}
