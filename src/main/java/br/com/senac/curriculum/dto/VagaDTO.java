package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.vaga.VagaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VagaDTO {

	private Long id;
	private String titulo;
	private String descricao;
	private String empresa;
	private String beneficios;
	private double salario;

	public VagaDTO() {
	}

	public VagaDTO(VagaEntity vagaEntity) {
		this.id = vagaEntity.getId();
		this.titulo = vagaEntity.getTitulo();
		this.descricao = vagaEntity.getDescricao();
		this.empresa = vagaEntity.getEmpresa();
		this.beneficios = vagaEntity.getBeneficios();
		this.salario = vagaEntity.getSalario();

	}

}
