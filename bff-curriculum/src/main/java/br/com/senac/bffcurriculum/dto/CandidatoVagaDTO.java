package br.com.senac.bffcurriculum.dto;

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
}
