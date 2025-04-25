package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EducacaoDTO {

	private Long id;
	private String grau;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private String instituicao;
	private String curso;

	public EducacaoDTO() {
	}

	public EducacaoDTO(EducacaoEntity educacaoEntity) {
		this.id = educacaoEntity.getId();
		this.grau = educacaoEntity.getGrau();
		this.dataInicio = educacaoEntity.getDataInicio();
		this.dataFim = educacaoEntity.getDataFim();
		this.instituicao = educacaoEntity.getInstituicao();
		this.curso = educacaoEntity.getCurso();
	}

}
