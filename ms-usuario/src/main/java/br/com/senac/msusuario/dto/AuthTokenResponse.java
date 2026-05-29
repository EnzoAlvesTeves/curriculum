package br.com.senac.msusuario.dto;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        Long refreshExpiresIn,
        String tokenType,
        String scope
) {
}

