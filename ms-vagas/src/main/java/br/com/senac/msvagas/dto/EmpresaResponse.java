package br.com.senac.msvagas.dto;

import java.time.LocalDateTime;

public record EmpresaResponse(
        Long id,
        String nome,
        String estado,
        String cidade,
        String bairro,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

