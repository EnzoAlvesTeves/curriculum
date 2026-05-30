package br.com.senac.mscurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducacaoDTO {
    private Long id;
    private Long idCandidato;
    private String curso;
    private String grau;
    private String instituicao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}

