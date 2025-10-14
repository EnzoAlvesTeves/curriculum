package br.com.senac.msvagas.dto;

import br.com.senac.msvagas.repository.entity.VagaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoVagaDTO {
	private Long id;
	private VagaDTO vaga;
	private LocalDateTime dataInscricao;
	private Long candidatoId;

	public CandidatoVagaDTO(Long id, VagaEntity vaga, LocalDateTime dataInscricao, Long candidatoId) {
		this.id = id;
		this.vaga = new VagaDTO(vaga);
		this.dataInscricao = dataInscricao;
		this.candidatoId = candidatoId;
	}
}
