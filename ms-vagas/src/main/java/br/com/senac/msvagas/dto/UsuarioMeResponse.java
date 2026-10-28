package br.com.senac.msvagas.dto;

public record UsuarioMeResponse(
        Long id,
        TipoUsuario tipo,
        String nome,
        String sobrenome,
        String email
) {
}

