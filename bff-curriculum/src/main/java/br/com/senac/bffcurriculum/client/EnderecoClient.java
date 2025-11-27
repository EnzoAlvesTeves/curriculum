package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.EnderecoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class EnderecoClient {
    private static final String BASE_URL = "http://localhost:8090/enderecos";

    private RestTemplate restTemplate;

    EnderecoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnderecoDTO create(EnderecoDTO enderecoDTO) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.POST,
                new HttpEntity<>(enderecoDTO),
                EnderecoDTO.class
        );

        return response.getBody();
    }

    public EnderecoDTO update(EnderecoDTO enderecoDTO) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.PUT,
                new HttpEntity<>(enderecoDTO),
                EnderecoDTO.class
        );

        return response.getBody();
    }

    public EnderecoDTO getById(Long id) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(BASE_URL + "/" + id),
                HttpMethod.GET,
                null,
                EnderecoDTO.class
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
