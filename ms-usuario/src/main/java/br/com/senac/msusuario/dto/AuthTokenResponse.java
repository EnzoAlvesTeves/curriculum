package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tokens retornados após autenticação bem-sucedida")
public record AuthTokenResponse(
        @Schema(description = "Token de acesso JWT", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Token para renovação do access token", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        String refreshToken,

        @Schema(description = "Tempo de expiração do access token em segundos", example = "300")
        Long expiresIn,

        @Schema(description = "Tempo de expiração do refresh token em segundos", example = "1800")
        Long refreshExpiresIn,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tokenType,

        @Schema(description = "Escopos concedidos ao token", example = "openid profile email")
        String scope
) {
}

