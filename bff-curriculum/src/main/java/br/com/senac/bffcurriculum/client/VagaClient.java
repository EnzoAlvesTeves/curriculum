package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class VagaClient {
    private static final String BASE_URL = "http://localhost:8092/vagas";

    private RestTemplate restTemplate;

    VagaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VagaDTO create(VagaDTO vagaDTO) throws URISyntaxException {
        ResponseEntity<VagaDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.POST,
                new HttpEntity<>(vagaDTO),
                VagaDTO.class
        );

        return response.getBody();
    }

    public VagaDTO update(VagaDTO vagaDTO) throws URISyntaxException {
        ResponseEntity<VagaDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.PUT,
                new HttpEntity<>(vagaDTO),
                VagaDTO.class
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

    public VagaDTO getById(Long id) throws URISyntaxException {
        ResponseEntity<VagaDTO> response = restTemplate.exchange(
                new URI(BASE_URL + "/" + id),
                HttpMethod.GET,
                null,
                VagaDTO.class
        );

        return response.getBody();
    }

    public List<VagaDTO> getall() throws URISyntaxException {
        ResponseEntity<VagaDTO[]> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.GET,
                null,
                VagaDTO[].class
        );

        return response.getBody() != null
                ? Arrays.stream(response.getBody()).toList()
                : new ArrayList<>();
    }

}
