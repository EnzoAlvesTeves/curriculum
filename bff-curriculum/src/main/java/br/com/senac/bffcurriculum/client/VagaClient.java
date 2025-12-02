package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${base-url.ms-vagas}")
    private String baseUrl;

    private static final String VAGAS = "/vagas";

    private RestTemplate restTemplate;

    VagaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VagaDTO create(VagaDTO vagaDTO) {
        ResponseEntity<VagaDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + VAGAS),
                    HttpMethod.POST,
                    new HttpEntity<>(vagaDTO),
                    VagaDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public VagaDTO update(VagaDTO vagaDTO) {
        ResponseEntity<VagaDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + VAGAS),
                    HttpMethod.PUT,
                    new HttpEntity<>(vagaDTO),
                    VagaDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public void delete(Long id) {
        try {
            restTemplate.exchange(
                    new URI(baseUrl + VAGAS + "/" + id),
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public VagaDTO getById(Long id) {
        ResponseEntity<VagaDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + VAGAS + "/" + id),
                    HttpMethod.GET,
                    null,
                    VagaDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public List<VagaDTO> getall() {
        ResponseEntity<VagaDTO[]> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + VAGAS),
                    HttpMethod.GET,
                    null,
                    VagaDTO[].class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody() != null
                ? Arrays.stream(response.getBody()).toList()
                : new ArrayList<>();
    }

}
