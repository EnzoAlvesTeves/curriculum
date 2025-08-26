package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.experiencia.ExperienciaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaDTO {

	private Long id;
	private String cargo;
	private String empresa;
	private LocalDate dataInicio;
	private LocalDate dataFim;

	public ExperienciaDTO(ExperienciaEntity experienciaEntity) {
		this.id = experienciaEntity.getId();
		this.cargo = experienciaEntity.getCargo();
		this.empresa = experienciaEntity.getEmpresa();
		this.dataInicio = experienciaEntity.getDataInicio();
		this.dataFim = experienciaEntity.getDataFim();
	}

}
