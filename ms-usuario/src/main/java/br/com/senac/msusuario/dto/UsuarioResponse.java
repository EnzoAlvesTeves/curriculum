package br.com.senac.msusuario.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String keycloakUserId,
        String nome,
        String sobrenome,
        String email,
        String telefone,
        String tipo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

