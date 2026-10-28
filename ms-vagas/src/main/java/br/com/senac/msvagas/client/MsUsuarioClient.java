package br.com.senac.msvagas.client;

import br.com.senac.msvagas.dto.UsuarioResponse;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface MsUsuarioClient {

    @RequestLine("GET /api/usuarios/me")
    @Headers({"Authorization: {authorization}"})
    UsuarioMeResponse buscarUsuarioLogado(@Param("authorization") String authorization);

    @RequestLine("GET /api/usuarios?ids={ids}")
    @Headers({"Authorization: {authorization}"})
    List<UsuarioResponse> buscarUsuariosPorIds(@Param("authorization") String authorization,
                                               @Param("ids") String ids);
}

