package br.com.senac.msusuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUsuarioRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Size(max = 150) String sobrenome,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 30) String telefone,
        @NotBlank @Size(max = 20) String tipo,
        @NotBlank @Size(min = 6) String senha
) {
}

