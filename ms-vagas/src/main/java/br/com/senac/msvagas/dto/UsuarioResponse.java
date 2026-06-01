package br.com.senac.msvagas.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String keycloakUserId,
        String nome,
        String sobrenome,
        String email,
        String telefone,
        TipoUsuario tipo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

