package br.com.senac.mscurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaDTO {
    private Long id;
    private Long idCandidato;
    private String cargo;
    private String empresa;
    private String resumo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}

