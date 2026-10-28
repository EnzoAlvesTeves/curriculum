package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para renovação do access token")
public record AuthRefreshTokenRequest(
        @Schema(description = "Refresh token válido retornado no login", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
        @NotBlank String refreshToken
) {
}

