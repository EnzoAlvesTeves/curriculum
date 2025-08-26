package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
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
    private CandidatoDTO candidato;
    private LocalDateTime dataInscricao;

    CandidatoVagaDTO(Long id, VagaEntity vaga, CandidatoEntity candidato, LocalDateTime dataInscricao) {
        this.id = id;
        this.vaga = new VagaDTO(vaga);
        this.candidato = new CandidatoDTO(candidato);
        this.dataInscricao = dataInscricao;
    }
}
