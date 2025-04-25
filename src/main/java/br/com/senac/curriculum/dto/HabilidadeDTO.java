package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.habilidade.HabilidadeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class HabilidadeDTO {

	private Long id;
	private String descricao;
	private String nivel;
	private String especialidade;

	public HabilidadeDTO() {
	}

	public HabilidadeDTO(HabilidadeEntity habilidadeEntity) {
		this.id = habilidadeEntity.getId();
		this.descricao = habilidadeEntity.getDescricao();
		this.nivel = habilidadeEntity.getNivel();
		this.especialidade = habilidadeEntity.getEspecialidade();
	}

}
