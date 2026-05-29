package br.com.senac.msvagas.client;

import br.com.senac.msvagas.dto.UsuarioMeResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface MsUsuarioClient {

    @RequestLine("GET /api/usuarios/me")
    @Headers({"Authorization: {authorization}"})
    UsuarioMeResponse buscarUsuarioLogado(@Param("authorization") String authorization);
}

