package br.com.senac.msvagas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VagaResponse(
        Long id,
        String titulo,
        String descricao,
        BigDecimal salario,
        String beneficios,
        Long idEmpresa,
        Long createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

