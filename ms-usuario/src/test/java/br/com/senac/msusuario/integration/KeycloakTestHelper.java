package br.com.senac.msusuario.integration;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Utilitário para obter tokens JWT do Keycloak de teste.
 * Usado para autenticar nas chamadas aos endpoints protegidos.
 */
public class KeycloakTestHelper {

    /**
     * Obtém um access token via password grant para um usuário já criado no Keycloak.
     *
     * @param keycloakBaseUrl URL base do Keycloak (ex: http://localhost:xxxxx)
     * @param realm           nome do realm
     * @param clientId        client ID público (ex: curriculum-app-user)
     * @param username        e-mail / username do usuário
     * @param password        senha do usuário
     * @return access_token JWT como String
     */
    public static String obterToken(String keycloakBaseUrl, String realm, String clientId,
                                    String username, String password) {
        RestClient restClient = RestClient.create();

        String tokenUrl = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("username", username);
        form.add("password", password);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (resp == null || resp.get("access_token") == null) {
            throw new IllegalStateException("Falha ao obter token do Keycloak para o usuário: " + username);
        }

        return String.valueOf(resp.get("access_token"));
    }
}
