package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.HabilidadeDTO;
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
public class HabilidadeClient {
    @Value("${base-url.ms-curriculum}")
    private String baseUrl;

    private static final String HABILIDADES = "/habilidades";

    private RestTemplate restTemplate;

    HabilidadeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HabilidadeDTO create(HabilidadeDTO habilidadeDTO) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO> response = restTemplate.exchange(
                new URI(baseUrl + HABILIDADES),
                HttpMethod.POST,
                new HttpEntity<>(habilidadeDTO),
                HabilidadeDTO.class
        );

        return response.getBody();
    }

    public HabilidadeDTO update(HabilidadeDTO habilidadeDTO) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO> response = restTemplate.exchange(
                new URI(baseUrl + HABILIDADES),
                HttpMethod.PUT,
                new HttpEntity<>(habilidadeDTO),
                HabilidadeDTO.class
        );

        return response.getBody();
    }

    public List<HabilidadeDTO> getByCandidatoId(Long candidatoId) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO[]> response = restTemplate.exchange(
                new URI(baseUrl + HABILIDADES + "/candidato/" + candidatoId),
                HttpMethod.GET,
                null,
                HabilidadeDTO[].class
        );

        return response.getBody() != null
                ? Arrays.stream(response.getBody()).toList()
                : new ArrayList<>();
    }

    public void delete(Long id) throws URISyntaxException {
        restTemplate.exchange(
                new URI(baseUrl + HABILIDADES + "/" + id),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
