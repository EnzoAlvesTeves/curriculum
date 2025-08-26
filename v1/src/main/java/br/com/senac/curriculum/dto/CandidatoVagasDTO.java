package br.com.senac.curriculum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CandidatoVagasDTO {

	private CandidatoDTO candidato;
	private List<VagaDTO> vagas;

	public CandidatoVagasDTO(CandidatoDTO candidato, List<VagaDTO> vagas) {
		this.candidato = candidato;
		this.vagas = vagas;
	}

}
