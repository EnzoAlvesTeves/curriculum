package br.com.senac.curriculum.dto;

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

}
