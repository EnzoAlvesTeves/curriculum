package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class HabilidadeClient {
    private static final String BASE_URL = "http://localhost:8090/habilidades";

    private RestTemplate restTemplate;

    HabilidadeClient(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) throws URISyntaxException {
        ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.POST,
                new HttpEntity<>(usuarioDTO),
                UsuarioDTO.class
        );

        return response.getBody();
    }

    public UsuarioDTO update(UsuarioDTO usuarioDTO) throws URISyntaxException {
        ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.PUT,
                new HttpEntity<>(usuarioDTO),
                UsuarioDTO.class
        );

        return response.getBody();
    }

    public UsuarioDTO getById(Long id) throws URISyntaxException {
        ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                new URI(BASE_URL + "/" + id),
                HttpMethod.GET,
                null,
                UsuarioDTO.class
        );

        return response.getBody();
    }

    public void delete(Long id) throws URISyntaxException {
        restTemplate.exchange(
                new URI(BASE_URL + "/" + id),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
