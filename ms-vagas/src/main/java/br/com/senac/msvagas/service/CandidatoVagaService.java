package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CandidatoVagaDTO;
import br.com.senac.msvagas.dto.VagaDTO;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoVagaService {

	private final CandidatoVagaRepository candidatoVagaRepository;

	public CandidatoVagaService(CandidatoVagaRepository candidatoVagaRepository) {
		this.candidatoVagaRepository = candidatoVagaRepository;
	}
}
