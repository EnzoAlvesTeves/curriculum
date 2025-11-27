package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UsuarioClient {
    private static final String BASE_URL = "http://localhost:8091/usuarios";

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
                    new URI(BASE_URL),
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
                    new URI(BASE_URL),
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
                    new URI(BASE_URL + "/" + id),
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
                    new URI(BASE_URL + "/" + id),
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
                    new URI(BASE_URL + "/login"),
                    HttpMethod.POST,
                    new HttpEntity<>(loginDTO),
                    UsuarioDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }
}
