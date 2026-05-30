package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados de retorno de um usuário")
public record UsuarioResponse(
        @Schema(description = "Identificador interno do usuário", example = "1")
        Long id,

        @Schema(description = "ID do usuário no Keycloak", example = "a3f2c1d4-12ab-4e56-bc78-9de0f1234567")
        String keycloakUserId,

        @Schema(description = "Primeiro nome", example = "João")
        String nome,

        @Schema(description = "Sobrenome", example = "Silva")
        String sobrenome,

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Telefone de contato", example = "(11) 91234-5678")
        String telefone,

        @Schema(description = "Tipo do usuário", example = "CANDIDATO")
        String tipo,

        @Schema(description = "Data e hora de criação do registro")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da última atualização do registro")
        LocalDateTime updatedAt
) {
}

