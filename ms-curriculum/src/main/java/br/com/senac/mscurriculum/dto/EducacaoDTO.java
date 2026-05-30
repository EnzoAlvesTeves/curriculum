package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Formação acadêmica vinculada ao candidato")
public class EducacaoDTO {
    @Schema(description = "Identificador da educação", example = "1")
    private Long id;
    @Schema(description = "Identificador do candidato", example = "1")
    private Long idCandidato;
    @Schema(description = "Curso", example = "Análise e Desenvolvimento de Sistemas")
    private String curso;
    @Schema(description = "Grau de formação", example = "Técnico")
    private String grau;
    @Schema(description = "Instituição de ensino", example = "Senac")
    private String instituicao;
    @Schema(description = "Data de início", example = "2014-01-01")
    private LocalDate dataInicio;
    @Schema(description = "Data de fim", example = "2016-12-31")
    private LocalDate dataFim;
}

