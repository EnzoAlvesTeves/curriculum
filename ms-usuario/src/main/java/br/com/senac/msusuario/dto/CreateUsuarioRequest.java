package br.com.senac.msusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo usuário")
public record CreateUsuarioRequest(
        @Schema(description = "Primeiro nome do usuário", example = "João", maxLength = 100)
        @NotBlank @Size(max = 100) String nome,

        @Schema(description = "Sobrenome do usuário", example = "Silva", maxLength = 150)
        @NotBlank @Size(max = 150) String sobrenome,

        @Schema(description = "E-mail do usuário (utilizado como login)", example = "joao.silva@email.com", maxLength = 150)
        @NotBlank @Email @Size(max = 150) String email,

        @Schema(description = "Telefone de contato", example = "(11) 91234-5678", maxLength = 30)
        @Size(max = 30) String telefone,

        @Schema(description = "Tipo do usuário: CANDIDATO ou RH", example = "CANDIDATO", maxLength = 20)
        @NotBlank @Size(max = 20) String tipo,

        @Schema(description = "Senha de acesso (mínimo 6 caracteres)", example = "senhaSegura123", minLength = 6)
        @NotBlank @Size(min = 6) String senha
) {
}

