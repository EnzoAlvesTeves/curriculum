package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para alteração de senha por username (login/e-mail)")
public record AlterarSenhaPorUsernameRequest(
        @Schema(description = "Username do usuário no sistema (atualmente o e-mail)", example = "joao.silva@email.com")
        @NotBlank String username,

        @Schema(description = "Nova senha (mínimo 6 caracteres)", example = "novaSenhaForte123", minLength = 6)
        @NotBlank @Size(min = 6) String novaSenha
) {
}

