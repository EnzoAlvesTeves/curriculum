package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.EnderecoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class EnderecoClient {
    @Value("${base-url.ms-curriculum}")
    private String baseUrl;

    private static final String ENDERECOS = "/enderecos";

    private RestTemplate restTemplate;

    EnderecoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EnderecoDTO create(EnderecoDTO enderecoDTO) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(baseUrl + ENDERECOS),
                HttpMethod.POST,
                new HttpEntity<>(enderecoDTO),
                EnderecoDTO.class
        );

        return response.getBody();
    }

    public EnderecoDTO update(EnderecoDTO enderecoDTO) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(baseUrl + ENDERECOS),
                HttpMethod.PUT,
                new HttpEntity<>(enderecoDTO),
                EnderecoDTO.class
        );

        return response.getBody();
    }

    public EnderecoDTO getById(Long id) throws URISyntaxException {
        ResponseEntity<EnderecoDTO> response = restTemplate.exchange(
                new URI(baseUrl + ENDERECOS + "/" + id),
                HttpMethod.GET,
                null,
                EnderecoDTO.class
        );

        return response.getBody();
    }

    public void delete(Long id) throws URISyntaxException {
        restTemplate.exchange(
                new URI(baseUrl + ENDERECOS + "/" + id),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
