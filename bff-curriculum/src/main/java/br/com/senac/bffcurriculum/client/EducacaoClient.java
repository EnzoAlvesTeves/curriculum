package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.EducacaoDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EducacaoClient {
	private static final String BASE_URL = "http://localhost:8090/educacoes";

	private RestTemplate restTemplate;

	EducacaoClient(
					RestTemplate restTemplate
	) {
		this.restTemplate = restTemplate;
	}

	public EducacaoDTO create(EducacaoDTO educacaoDTO) throws URISyntaxException {
		ResponseEntity<EducacaoDTO> response = restTemplate.exchange(
						new URI(BASE_URL),
						HttpMethod.POST,
						new HttpEntity<>(educacaoDTO),
						EducacaoDTO.class
		);
		return response.getBody();
	}

	public List<EducacaoDTO> getByCandidatoId(Long candidatoId) throws URISyntaxException {
		ResponseEntity<EducacaoDTO[]> response = restTemplate.exchange(
						new URI(BASE_URL + "/candidato/" + candidatoId),
						HttpMethod.GET,
						null,
						EducacaoDTO[].class
		);

		return  response.getBody() != null
						? Arrays.stream(response.getBody()).toList()
						: new ArrayList<>();
	}

	public EducacaoDTO update(EducacaoDTO educacaoDTO) throws URISyntaxException {
		ResponseEntity<EducacaoDTO> response = restTemplate.exchange(
						new URI(BASE_URL),
						HttpMethod.PUT,
						new HttpEntity<>(educacaoDTO),
						EducacaoDTO.class
		);

		return response.getBody();
	}

	public void delete( Long educacaoId) throws URISyntaxException{
		restTemplate.exchange(
						new URI(BASE_URL + "/" + educacaoId),
						HttpMethod.DELETE,
						null,
						Void.class
		);
	}
}
