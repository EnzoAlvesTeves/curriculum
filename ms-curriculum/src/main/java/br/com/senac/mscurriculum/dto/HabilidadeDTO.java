package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Habilidade vinculada ao candidato")
public class HabilidadeDTO {
    @Schema(description = "Identificador da habilidade", example = "1")
    private Long id;
    @Schema(description = "Identificador do candidato", example = "1")
    private Long idCandidato;
    @Schema(description = "Descrição da habilidade", example = "Java")
    private String descricao;
    @Schema(description = "Nível da habilidade", example = "Avançado")
    private String nivel;
}

