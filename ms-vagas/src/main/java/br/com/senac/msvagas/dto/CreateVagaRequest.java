package br.com.senac.msvagas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateVagaRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank String descricao,
        @DecimalMin(value = "0.0", inclusive = true) BigDecimal salario,
        String beneficios,
        @NotNull Long idEmpresa
) {
}

