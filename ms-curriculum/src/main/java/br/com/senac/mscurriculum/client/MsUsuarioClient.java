package br.com.senac.mscurriculum.client;

import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface MsUsuarioClient {

    @RequestLine("GET /api/usuarios/me")
    @Headers({"Authorization: {authorization}"})
    UsuarioMeResponse buscarUsuarioLogado(@Param("authorization") String authorization);
}
