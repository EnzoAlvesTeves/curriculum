package br.com.senac.mscurriculum.dto;

import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
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
	private Long candidatoId;

	public ExperienciaDTO(ExperienciaEntity experienciaEntity) {
		this.id = experienciaEntity.getId();
		this.cargo = experienciaEntity.getCargo();
		this.empresa = experienciaEntity.getEmpresa();
		this.dataInicio = experienciaEntity.getDataInicio();
		this.dataFim = experienciaEntity.getDataFim();
		this.candidatoId = experienciaEntity.getCandidato().getId();
	}

	public ExperienciaDTO(ExperienciaEntity experiencia, Long id) {
		this.id = experiencia.getId();
		this.cargo = experiencia.getCargo();
		this.empresa = experiencia.getEmpresa();
		this.dataInicio = experiencia.getDataInicio();
		this.dataFim = experiencia.getDataFim();
		this.candidatoId = id;
	}

	public ExperienciaEntity toEntity() {
		ExperienciaEntity entity = new ExperienciaEntity();
		entity.setId(this.id);
		entity.setCargo(this.cargo);
		entity.setEmpresa(this.empresa);
		entity.setDataInicio(this.dataInicio);
		entity.setDataFim(this.dataFim);
		return entity;
	}
}
