package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.HabilidadeDTO;
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
    private static final String BASE_URL = "http://localhost:8090/habilidades";

    private RestTemplate restTemplate;

    HabilidadeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HabilidadeDTO create(HabilidadeDTO habilidadeDTO) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.POST,
                new HttpEntity<>(habilidadeDTO),
                HabilidadeDTO.class
        );

        return response.getBody();
    }

    public HabilidadeDTO update(HabilidadeDTO habilidadeDTO) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.PUT,
                new HttpEntity<>(habilidadeDTO),
                HabilidadeDTO.class
        );

        return response.getBody();
    }

    public List<HabilidadeDTO> getByCandidatoId(Long candidatoId) throws URISyntaxException {
        ResponseEntity<HabilidadeDTO[]> response = restTemplate.exchange(
                new URI(BASE_URL + "/candidato/" + candidatoId),
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
                new URI(BASE_URL + "/" + id),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
