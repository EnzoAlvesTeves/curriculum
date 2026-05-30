package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para alteração da própria senha")
public record AlterarSenhaRequest(
        @Schema(description = "Senha atual do usuário", example = "senhaAtual123")
        @NotBlank String senhaAtual,

        @Schema(description = "Nova senha (mínimo 6 caracteres)", example = "novaSenhaForte123", minLength = 6)
        @NotBlank @Size(min = 6) String novaSenha
) {
}

