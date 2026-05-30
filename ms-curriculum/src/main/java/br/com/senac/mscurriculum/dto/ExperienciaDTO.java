package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Experiência profissional vinculada ao candidato")
public class ExperienciaDTO {
    @Schema(description = "Identificador da experiência", example = "1")
    private Long id;
    @Schema(description = "Identificador do candidato", example = "1")
    private Long idCandidato;
    @Schema(description = "Cargo ocupado", example = "Desenvolvedor Pleno")
    private String cargo;
    @Schema(description = "Empresa", example = "Nome da Empresa")
    private String empresa;
    @Schema(description = "Resumo da experiência", example = "Desenvolvimento de APIs Java")
    private String resumo;
    @Schema(description = "Data de início", example = "2019-01-01")
    private LocalDate dataInicio;
    @Schema(description = "Data de fim", example = "2020-12-31")
    private LocalDate dataFim;
}

