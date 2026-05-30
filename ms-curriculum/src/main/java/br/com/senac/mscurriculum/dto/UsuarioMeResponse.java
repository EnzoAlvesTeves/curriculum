package br.com.senac.mscurriculum.dto;

public record UsuarioMeResponse(
        Long id,
        TipoUsuario tipo,
        String nome,
        String sobrenome,
        String email
) {
}
