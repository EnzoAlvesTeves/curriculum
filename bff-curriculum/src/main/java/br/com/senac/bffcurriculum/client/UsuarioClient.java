package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UsuarioClient {
    @Value("${base-url.ms-usuario}")
    private String baseUrl;

    private static final String USUARIOS = "/usuarios";

    private RestTemplate restTemplate;

    UsuarioClient(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        ResponseEntity<UsuarioDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + USUARIOS),
                    HttpMethod.POST,
                    new HttpEntity<>(usuarioDTO),
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public UsuarioDTO update(UsuarioDTO usuarioDTO) {
        ResponseEntity<UsuarioDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + USUARIOS),
                    HttpMethod.PUT,
                    new HttpEntity<>(usuarioDTO),
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public UsuarioDTO getById(Long id) {
        ResponseEntity<UsuarioDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + USUARIOS + "/" + id),
                    HttpMethod.GET,
                    null,
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public void delete(Long id) {
        try {
            restTemplate.exchange(
                    new URI(baseUrl + USUARIOS + "/" + id),
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public UsuarioDTO login(LoginRequestDTO loginDTO) {
        ResponseEntity<UsuarioDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + USUARIOS + "/login"),
                    HttpMethod.POST,
                    new HttpEntity<>(loginDTO),
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public UsuarioDTO getByEmail(String email) {
        ResponseEntity<UsuarioDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + USUARIOS + "/email/" + email),
                    HttpMethod.GET,
                    null,
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }
}
