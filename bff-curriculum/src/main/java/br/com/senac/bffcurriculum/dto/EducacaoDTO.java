package br.com.senac.bffcurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducacaoDTO {
    private Long id;
    private String grau;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String instituicao;
    private String curso;
    private Long candidatoId;
}
