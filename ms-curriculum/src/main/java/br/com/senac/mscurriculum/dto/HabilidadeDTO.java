package br.com.senac.mscurriculum.dto;

import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabilidadeDTO {

    private Long id;
    private String descricao;
    private String nivel;
    private String especialidade;
    private Long candidatoId;

    public HabilidadeDTO(HabilidadeEntity habilidadeEntity) {
        this.id = habilidadeEntity.getId();
        this.descricao = habilidadeEntity.getDescricao();
        this.nivel = habilidadeEntity.getNivel();
        this.especialidade = habilidadeEntity.getEspecialidade();
        this.candidatoId = habilidadeEntity.getCandidato().getId();
    }

    public HabilidadeDTO(HabilidadeEntity habilidadeEntity, Long candidatoId) {
        this.id = habilidadeEntity.getId();
        this.descricao = habilidadeEntity.getDescricao();
        this.nivel = habilidadeEntity.getNivel();
        this.especialidade = habilidadeEntity.getEspecialidade();
        this.candidatoId = candidatoId;
    }
}
