package br.com.senac.msvagas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateEmpresaRequest(
        @NotBlank @Size(max = 255) String nome,
        @NotBlank @Size(max = 255) String estado,
        @NotBlank @Size(max = 255) String cidade,
        @Size(max = 255) String bairro
) {
}

