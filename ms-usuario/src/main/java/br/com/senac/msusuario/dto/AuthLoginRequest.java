package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public record AuthLoginRequest(
        @Schema(description = "Nome de usuário ou e-mail", example = "joao.silva@email.com")
        @NotBlank String username,

        @Schema(description = "Senha do usuário", example = "senhaSegura123")
        @NotBlank String senha
) {
}

