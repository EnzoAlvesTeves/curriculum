package br.com.senac.mscurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabilidadeDTO {
    private Long id;
    private Long idCandidato;
    private String descricao;
    private String nivel;
}

