package br.com.senac.msvagas.dto;

import java.time.LocalDateTime;

public record CandidaturaResponse(
        Long id,
        Long idUsuario,
        Long idVaga,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

