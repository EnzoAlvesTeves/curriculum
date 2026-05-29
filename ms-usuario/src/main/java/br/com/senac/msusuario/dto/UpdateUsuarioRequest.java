package br.com.senac.msusuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Size(max = 150) String sobrenome,
        @Size(max = 30) String telefone,
        @NotBlank @Size(max = 20) String tipo
) {
}

